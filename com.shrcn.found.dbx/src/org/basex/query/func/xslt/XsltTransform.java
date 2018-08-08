package org.basex.query.func.xslt;

import static org.basex.query.QueryError.BXSL_ERROR_X;
import static org.basex.query.QueryError.IOERR_X;
import static org.basex.query.QueryError.STRNOD_X_X;
import static org.basex.util.Token.string;
import static org.basex.util.Token.trim;
import static org.basex.util.Token.utf8;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.basex.io.IO;
import org.basex.io.IOContent;
import org.basex.io.out.ArrayOutput;
import org.basex.io.serial.SerializerMode;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryIOException;
import org.basex.query.expr.Expr;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.ANode;
import org.basex.query.value.node.DBNode;
import org.basex.util.InputInfo;
import org.basex.util.Prop;
import org.basex.util.options.Options;

/**
 * Functions for performing XSLT transformations.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class XsltTransform extends XsltFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    try {
      return new DBNode(new IOContent(transform(qc)));
    } catch(final IOException ex) {
      throw IOERR_X.get(info, ex);
    }
  }

  /**
   * Performs an XSL transformation.
   * @param qc query context
   * @return item
   * @throws QueryException query exception
   */
  final byte[] transform(final QueryContext qc) throws QueryException {
    checkCreate(qc);
    final IO in = read(exprs[0], qc);
    final IO xsl = read(exprs[1], qc);
    final Options opts = toOptions(2, new Options(), qc);

    final PrintStream tmp = System.err;
    final ArrayOutput ao = new ArrayOutput();
    try {
      System.setErr(new PrintStream(ao));
      return transform(in, xsl, opts.free());
    } catch(final TransformerException ex) {
      throw BXSL_ERROR_X.get(info, trim(utf8(ao.finish(), Prop.ENCODING)));
    } finally {
      System.setErr(tmp);
    }
  }

  /**
   * Returns an input reference (possibly cached) to the specified input.
   * @param ex expression to be evaluated
   * @param qc query context
   * @return item
   * @throws QueryException query exception
   */
  private IO read(final Expr ex, final QueryContext qc) throws QueryException {
    final Item it = toNodeOrAtomItem(ex, qc);
    if(it instanceof ANode) {
      try {
        final IO io = new IOContent(it.serialize(SerializerMode.NOINDENT.get()).finish());
        io.name(string(((ANode) it).baseURI()));
        return io;
      } catch(final QueryIOException e) {
        e.getCause(info);
      }
    }
    if(it.type.isStringOrUntyped()) return checkPath(toToken(it));
    throw STRNOD_X_X.get(info, it.type, it);
  }

  /**
   * Uses Java's XSLT implementation to perform an XSL transformation.
   * @param in input
   * @param xsl style sheet
   * @param par parameters
   * @return transformed result
   * @throws TransformerException transformer exception
   */
  private static byte[] transform(final IO in, final IO xsl, final HashMap<String, String> par)
      throws TransformerException {

    // create transformer
    final TransformerFactory tc = TransformerFactory.newInstance();
    final Transformer tr = tc.newTransformer(xsl.streamSource());

    // bind parameters
    for(final Entry<String, String> entry : par.entrySet())
      tr.setParameter(entry.getKey(), entry.getValue());

    // do transformation and return result
    final ArrayOutput ao = new ArrayOutput();
    tr.transform(in.streamSource(), new StreamResult(ao));
    return ao.finish();
  }
}
