package org.basex.query.expr.constr;

import static org.basex.query.QueryError.CAINV_;
import static org.basex.query.QueryError.CAXML;
import static org.basex.query.QueryError.INVPREF_X;
import static org.basex.query.QueryText.ATTRIBUTE;
import static org.basex.query.QueryText.XMLNS_URI;
import static org.basex.query.QueryText.XML_URI;
import static org.basex.util.Token.EMPTY;
import static org.basex.util.Token.ID;
import static org.basex.util.Token.XML;
import static org.basex.util.Token.XMLNS;
import static org.basex.util.Token.concat;
import static org.basex.util.Token.eq;
import static org.basex.util.Token.normalize;
import static org.basex.util.Token.token;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.StaticContext;
import org.basex.query.expr.Expr;
import org.basex.query.value.item.QNm;
import org.basex.query.value.node.FAttr;
import org.basex.query.value.type.SeqType;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * Attribute constructor.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class CAttr extends CName {
  /** Generated namespace. */
  private static final byte[] NS0 = token("ns0:");
  /** Computed constructor. */
  private final boolean comp;

  /**
   * Constructor.
   * @param sc static context
   * @param info input info
   * @param comp computed construction flag
   * @param name name
   * @param values attribute values
   */
  public CAttr(final StaticContext sc, final InputInfo info, final boolean comp, final Expr name,
      final Expr... values) {
    super(ATTRIBUTE, sc, info, name, values);
    this.comp = comp;
    seqType = SeqType.ATT;
  }

  @Override
  public FAttr item(final QueryContext qc, final InputInfo ii) throws QueryException {
    QNm nm = qname(qc, false);
    final byte[] cp = nm.prefix();
    if(comp) {
      final byte[] cu = nm.uri();
      if(eq(cp, XML) ^ eq(cu, XML_URI)) throw CAXML.get(info);
      if(eq(cu, XMLNS_URI)) throw CAINV_.get(info, cu);
      if(eq(cp, XMLNS) || cp.length == 0 && eq(nm.string(), XMLNS))
        throw CAINV_.get(info, nm.string());

      // create new standard namespace to cover most frequent cases
      if(eq(cp, EMPTY) && !eq(cu, EMPTY))
        nm = new QNm(concat(NS0, nm.string()), cu);
    }
    if(!nm.hasURI() && nm.hasPrefix()) throw INVPREF_X.get(info, nm);

    byte[] val = atomValue(qc);
    if(eq(cp, XML) && eq(nm.local(), ID)) val = normalize(val);

    return new FAttr(nm, val);
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new CAttr(sc, info, comp, name.copy(cc, vm), copyAll(cc, vm, exprs));
  }
}
