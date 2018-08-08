package org.basex.query.expr.constr;

import static org.basex.query.QueryError.CNINVNS_X;
import static org.basex.query.QueryError.CNINV_X;
import static org.basex.query.QueryError.CNXML;
import static org.basex.query.QueryError.INVNSNAME_X;
import static org.basex.query.QueryText.NAMESPACE;
import static org.basex.query.QueryText.XMLNS_URI;
import static org.basex.query.QueryText.XML_URI;
import static org.basex.util.Token.XML;
import static org.basex.util.Token.XMLNS;
import static org.basex.util.Token.eq;
import static org.basex.util.Token.trim;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.StaticContext;
import org.basex.query.expr.Expr;
import org.basex.query.value.node.FNSpace;
import org.basex.query.value.type.SeqType;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.XMLToken;
import org.basex.util.hash.IntObjMap;

/**
 * Namespace constructor.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class CNSpace extends CName {
  /**
   * Constructor.
   * @param sc static context
   * @param info input info
   * @param name name
   * @param value value
   */
  public CNSpace(final StaticContext sc, final InputInfo info, final Expr name, final Expr value) {
    super(NAMESPACE, sc, info, name, value);
    seqType = SeqType.NSP;
  }

  @Override
  public FNSpace item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] cp = toEmptyToken(name, qc);
    if(cp.length != 0 && !XMLToken.isNCName(cp)) throw INVNSNAME_X.get(info, cp);

    final byte[] cu = trim(atomValue(qc));
    if(eq(cp, XML) ^ eq(cu, XML_URI)) throw CNXML.get(info);
    if(eq(cp, XMLNS)) throw CNINV_X.get(info, cp);
    if(eq(cu, XMLNS_URI) || cu.length == 0) throw CNINVNS_X.get(info, cu);

    return new FNSpace(cp, cu);
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new CNSpace(sc, info, name.copy(cc, vm), exprs[0].copy(cc, vm));
  }
}
