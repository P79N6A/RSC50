package org.basex.query.func.crypto;

import static org.basex.query.QueryError.CX_ALGEXC;
import static org.basex.query.QueryError.CX_ALINV_X;
import static org.basex.query.QueryError.CX_CANINV;
import static org.basex.query.QueryError.CX_DIGINV;
import static org.basex.query.QueryError.CX_INVNM;
import static org.basex.query.QueryError.CX_IOEXC;
import static org.basex.query.QueryError.CX_KSEXC;
import static org.basex.query.QueryError.CX_KSNULL_X;
import static org.basex.query.QueryError.CX_NOKEY;
import static org.basex.query.QueryError.CX_NOSIG;
import static org.basex.query.QueryError.CX_SIGEXC;
import static org.basex.query.QueryError.CX_SIGINV;
import static org.basex.query.QueryError.CX_SIGTYPINV;
import static org.basex.query.QueryError.CX_XPINV;
import static org.basex.util.Token.eq;
import static org.basex.util.Token.lc;
import static org.basex.util.Token.string;
import static org.basex.util.Token.token;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.keyinfo.X509IssuerSerial;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.crypto.dsig.spec.XPathFilterParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.basex.io.out.ArrayOutput;
import org.basex.io.serial.Serializer;
import org.basex.io.serial.SerializerMode;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Bln;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.ANode;
import org.basex.query.value.type.NodeType;
import org.basex.util.InputInfo;
import org.basex.util.hash.TokenMap;
import org.basex.util.hash.TokenSet;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class generates and validates digital signatures for XML data.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Lukas Kircher
 */
final class DigitalSignature {
  /** Canonicalization algorithms. */
  private static final TokenMap CANONICALIZATIONS = new TokenMap();
  /** Signature digest algorithms. */
  private static final TokenMap DIGESTS = new TokenMap();
  /** Signature algorithms. */
  private static final TokenMap SIGNATURES = new TokenMap();
  /** Signature types. */
  private static final TokenSet TYPES = new TokenSet();
  /** Default canonicalization algorithm. */
  private static final byte[] DEFC = token("inclusive-with-comments");
  /** Default digest algorithm. */
  private static final byte[] DEFD = token("sha1");
  /** Default signature algorithm. */
  private static final byte[] DEFS = token("rsa_sha1");
  /** Default signature type enveloped. */
  private static final byte[] DEFT = token("enveloped");
  /** Signature type enveloping. */
  private static final byte[] ENVT = token("enveloping");

  // initializations
  static {
    CANONICALIZATIONS.put("inclusive-with-comments",
        CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS);
    CANONICALIZATIONS.put("exclusive-with-comments",
        CanonicalizationMethod.EXCLUSIVE_WITH_COMMENTS);
    CANONICALIZATIONS.put("inclusive",
        CanonicalizationMethod.INCLUSIVE);
    CANONICALIZATIONS.put("exclusive",
        CanonicalizationMethod.EXCLUSIVE);

    DIGESTS.put("sha1", DigestMethod.SHA1);
    DIGESTS.put("sha256", DigestMethod.SHA256);
    DIGESTS.put("sha512", DigestMethod.SHA512);

    SIGNATURES.put("rsa_sha1", SignatureMethod.RSA_SHA1);
    SIGNATURES.put("dsa_sha1", SignatureMethod.DSA_SHA1);

    TYPES.add(DEFT);
    TYPES.add(ENVT);
  }

  /** Input info. */
  private final InputInfo info;

  /**
   * Constructor.
   * @param info input info
   */
  DigitalSignature(final InputInfo info) {
    this.info = info;
  }

  /**
   * Generates a signature.
   * @param node node to be signed
   * @param c canonicalization algorithm
   * @param d digest algorithm
   * @param sig signature algorithm
   * @param ns signature element namespace prefix
   * @param t signature type (enveloped, enveloping, detached)
   * @param expr XPath expression which specifies node to be signed
   * @param ce certificate which contains keystore information for signing the node, may be null
   * @param qc query context
   * @param ii input info
   *
   * @return signed node
   * @throws QueryException query exception
   */
  Item generateSignature(final ANode node, final byte[] c, final byte[] d, final byte[] sig,
      final byte[] ns, final byte[] t, final byte[] expr, final ANode ce, final QueryContext qc,
      final InputInfo ii) throws QueryException {

    // checking input variables
    byte[] b = c;
    if(b.length == 0) b = DEFC;
    b = CANONICALIZATIONS.get(lc(b));
    if(b == null) throw CX_CANINV.get(info, c);
    final String canonicalization = string(b);

    b = d;
    if(b.length == 0) b = DEFD;
    b = DIGESTS.get(lc(b));
    if(b == null) throw CX_DIGINV.get(info, d);
    final String digest = string(b);

    b = sig;
    if(b.length == 0) b = DEFS;
    final byte[] tsig = b;
    b = SIGNATURES.get(lc(b));
    if(b == null) throw CX_SIGINV.get(info, sig);
    final String signature = string(b);
    final String keytype = string(tsig).substring(0, 3);

    b = t;
    if(b.length == 0) b = DEFT;
    if(!TYPES.contains(lc(b))) throw CX_SIGTYPINV.get(info, t);
    final byte[] type = b;

    final Item signedNode;
    try {
      final XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
      final PrivateKey pk;
      final KeyInfo ki;

      // dealing with given certificate details to initialize the keystore
      if(ce != null) {
        final Document ceDOM = toDOMNode(ce);
        if(!"digital-certificate".equals(ceDOM.getDocumentElement().getNodeName()))
          throw CX_INVNM.get(info, ceDOM);
        final NodeList ceChildren = ceDOM.getDocumentElement().getChildNodes();
        final int s = ceChildren.getLength();
        int ci = 0;
        // iterate child axis to retrieve keystore setup
        String ksURI = null, pkPW = null, kAlias = null, ksPW = null, ksTY = null;
        while(ci < s) {
          final Node cn = ceChildren.item(ci++);
          final String name = cn.getNodeName();
          if("keystore-type".equals(name)) ksTY = cn.getTextContent();
          else if("keystore-password".equals(name)) ksPW = cn.getTextContent();
          else if("key-alias".equals(name)) kAlias = cn.getTextContent();
          else if("private-key-password".equals(name)) pkPW = cn.getTextContent();
          else if("keystore-uri".equals(name)) ksURI = cn.getTextContent();
        }

        // initialize the keystore
        final KeyStore ks;
        try {
          ks = KeyStore.getInstance(ksTY);
        } catch(final KeyStoreException ex) {
          throw CX_KSNULL_X.get(info, ex);
        }

        try(final FileInputStream fis = new FileInputStream(ksURI)) {
          ks.load(fis, ksPW.toCharArray());
        }
        pk = (PrivateKey) ks.getKey(kAlias, pkPW.toCharArray());
        final X509Certificate x509ce = (X509Certificate) ks.getCertificate(kAlias);
        if(x509ce == null) throw CX_ALINV_X.get(info, kAlias);
        final PublicKey puk = x509ce.getPublicKey();
        final KeyInfoFactory kifactory = fac.getKeyInfoFactory();
        final KeyValue keyValue = kifactory.newKeyValue(puk);
        final Vector<XMLStructure> kiCont = new Vector<>();
        kiCont.add(keyValue);
        final List<Object> x509Content = new ArrayList<>();
        final X509IssuerSerial issuer = kifactory.newX509IssuerSerial(x509ce.
            getIssuerX500Principal().getName(), x509ce.getSerialNumber());
        x509Content.add(x509ce.getSubjectX500Principal().getName());
        x509Content.add(issuer);
        x509Content.add(x509ce);
        final X509Data x509Data = kifactory.newX509Data(x509Content);
        kiCont.add(x509Data);
        ki = kifactory.newKeyInfo(kiCont);

      // auto-generate keys if no certificate is provided
      } else {
        final KeyPairGenerator gen = KeyPairGenerator.getInstance(keytype);
        gen.initialize(512);
        final KeyPair kp = gen.generateKeyPair();
        final KeyInfoFactory kif = fac.getKeyInfoFactory();
        final KeyValue kv = kif.newKeyValue(kp.getPublic());
        ki = kif.newKeyInfo(Collections.singletonList(kv));
        pk = kp.getPrivate();
      }

      final Document inputNode = toDOMNode(node);
      final List<Transform> tfList;

      // validating a given XPath expression to get nodes to be signed
      if(expr.length > 0) {
        final XPathFactory xpf = XPathFactory.newInstance();
        final XPathExpression xExpr = xpf.newXPath().compile(string(expr));
        final NodeList xRes = (NodeList) xExpr.evaluate(inputNode, XPathConstants.NODESET);
        if(xRes.getLength() < 1) throw CX_XPINV.get(info, expr);
        tfList = new ArrayList<>(2);
        tfList.add(fac.newTransform(Transform.XPATH, new XPathFilterParameterSpec(string(expr))));
        tfList.add(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));

      } else {
        tfList = Collections.singletonList(fac.newTransform(
            Transform.ENVELOPED, (TransformParameterSpec) null));
      }

      // creating reference element
      final Reference ref = fac.newReference("",
          fac.newDigestMethod(digest, null), tfList, null, null);

      // creating signed info element
      final SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(
          canonicalization, (C14NMethodParameterSpec) null),
          fac.newSignatureMethod(signature, null), Collections.singletonList(ref));

      // prepare document signature
      final DOMSignContext signContext;
      final XMLSignature xmlSig;

      // enveloped signature
      if(eq(type, DEFT)) {
        signContext = new DOMSignContext(pk, inputNode.getDocumentElement());
        xmlSig = fac.newXMLSignature(si, ki);
      } else {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        final XMLStructure cont = new DOMStructure(inputNode.getDocumentElement());
        final XMLObject obj = fac.newXMLObject(Collections.singletonList(cont), "", null, null);
        xmlSig = fac.newXMLSignature(si, ki, Collections.singletonList(obj), null, null);
        signContext = new DOMSignContext(pk, inputNode);
      }

      // set Signature element namespace prefix if given
      if(ns.length > 0) signContext.setDefaultNamespacePrefix(string(ns));

      // actually sign the document
      xmlSig.sign(signContext);
      signedNode = NodeType.DOC.cast(inputNode, qc, null, ii);

    } catch(final XPathExpressionException e) {
      throw CX_XPINV.get(info, e);
    } catch(final SAXException | IOException | ParserConfigurationException e) {
      throw CX_IOEXC.get(info, e);
    } catch(final KeyStoreException e) {
      throw CX_KSEXC.get(info, e);
    } catch(final MarshalException |  XMLSignatureException e) {
      throw CX_SIGEXC.get(info, e);
    } catch(final NoSuchAlgorithmException | CertificateException |
        InvalidAlgorithmParameterException e) {
      throw CX_ALGEXC.get(info, e);
    } catch(final UnrecoverableKeyException | KeyException e) {
      throw CX_NOKEY.get(info, e);
    }
    return signedNode;
  }

  /**
   * Validates a signature.
   * @param node input node
   *
   * @return true if signature valid
   * @throws QueryException query exception
   */
  Item validateSignature(final ANode node) throws QueryException {
    try {
      final Document doc = toDOMNode(node);
      final DOMValidateContext valContext = new DOMValidateContext(new MyKeySelector(), doc);
      final NodeList signl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
      if(signl.getLength() < 1) throw CX_NOSIG.get(info, node);
      valContext.setNode(signl.item(0));
      final XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
      final XMLSignature signature = fac.unmarshalXMLSignature(valContext);
      return Bln.get(signature.validate(valContext));

    } catch(final XMLSignatureException | SAXException | ParserConfigurationException |
        IOException e) {
      throw CX_IOEXC.get(info, e);
    } catch(final MarshalException e) {
      throw CX_SIGEXC.get(info, e);
    }
  }

  /**
   * Serializes the given XML node to a byte array.
   * @param node node to be serialized
   * @return byte array containing XML
   * @throws IOException exception
   */
  private static byte[] nodeToBytes(final ANode node) throws IOException {
    final ArrayOutput ao = new ArrayOutput();
    try(final Serializer ser = Serializer.get(ao, SerializerMode.NOINDENT.get())) {
      ser.serialize(node);
    }
    return ao.finish();
  }

  /**
   * Creates a DOM node for the given input node.
   * @param node node
   * @return DOM node representation of input node
   * @throws SAXException exception
   * @throws IOException exception
   * @throws ParserConfigurationException exception
   */
  private static Document toDOMNode(final ANode node)
      throws SAXException, IOException, ParserConfigurationException {

    final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    return dbf.newDocumentBuilder().parse(new ByteArrayInputStream(nodeToBytes(node)));
  }
}