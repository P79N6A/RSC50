package org.basex.query.up.expr;

import static org.basex.query.QueryError.UPNSCONFL_X_X;
import static org.basex.util.Token.eq;

import org.basex.query.QueryException;
import org.basex.query.StaticContext;
import org.basex.query.expr.Arr;
import org.basex.query.expr.Expr;
import org.basex.query.util.list.ANodeList;
import org.basex.query.value.item.QNm;
import org.basex.query.value.node.ANode;
import org.basex.query.value.type.SeqType;
import org.basex.util.InputInfo;

/**
 * Abstract update expression.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
abstract class Update extends Arr {
  /** Static context. */
  final StaticContext sc;

  /**
   * Constructor.
   * @param sc static context
   * @param info input info
   * @param expr expressions
   */
  Update(final StaticContext sc, final InputInfo info, final Expr... expr) {
    super(info, expr);
    this.sc = sc;
    seqType = SeqType.EMP;
  }

  @Override
  public boolean has(final Flag flag) {
    return flag == Flag.UPD || super.has(flag);
  }

  /**
   * Checks if the new namespaces have conflicting namespaces.
   * @param list node list
   * @param targ target node
   * @return specified node list
   * @throws QueryException query exception
   */
  final ANodeList checkNS(final ANodeList list, final ANode targ) throws QueryException {
    for(final ANode n : list) {
      final QNm name = n.qname();
      final byte[] pref = name.prefix();
      // attributes without prefix have no namespace
      if(pref.length == 0) continue;
      // check if attribute and target have the same namespace
      final byte[] uri = targ.uri(pref);
      if(uri != null && !eq(name.uri(), uri)) throw UPNSCONFL_X_X.get(info, name.uri(), uri);
    }
    return list;
  }
}
