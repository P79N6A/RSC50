package org.basex.query.func.fn;

import static org.basex.query.QueryError.NSDECL_X;
import static org.basex.query.QueryError.valueError;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.QNm;
import org.basex.query.value.node.ANode;
import org.basex.query.value.type.AtomType;
import org.basex.util.InputInfo;
import org.basex.util.XMLToken;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnResolveQName extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Item it = exprs[0].atomItem(qc, info);
    if(it == null) return null;
    final ANode base = toElem(exprs[1], qc);

    final byte[] name = toToken(it);
    if(!XMLToken.isQName(name)) throw valueError(AtomType.QNM, name, info);

    final QNm nm = new QNm(name);
    final byte[] pref = nm.prefix();
    byte[] uri = base.uri(pref);
    if(uri == null) uri = sc.ns.uri(pref);
    if(uri == null) throw NSDECL_X.get(info, pref);
    nm.uri(uri);
    return nm;
  }
}
