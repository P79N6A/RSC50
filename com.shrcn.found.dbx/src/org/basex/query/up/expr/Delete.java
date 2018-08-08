package org.basex.query.up.expr;

import static org.basex.query.QueryError.UPTRGDELEMPT_X;
import static org.basex.query.QueryText.DELETE;
import static org.basex.query.QueryText.NODES;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.StaticContext;
import org.basex.query.expr.Expr;
import org.basex.query.iter.Iter;
import org.basex.query.up.Updates;
import org.basex.query.up.primitives.node.DeleteNode;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.ANode;
import org.basex.query.value.node.DBNode;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * Delete expression.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Lukas Kircher
 */
public final class Delete extends Update {
  /**
   * Constructor.
   * @param sc static context
   * @param info input info
   * @param trg target expression
   */
  public Delete(final StaticContext sc, final InputInfo info, final Expr trg) {
    super(sc, info, trg);
  }

  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Iter t = qc.iter(exprs[0]);
    for(Item i; (i = t.next()) != null;) {
      if(!(i instanceof ANode)) throw UPTRGDELEMPT_X.get(info, i);
      final ANode n = (ANode) i;
      // nodes without parents are ignored
      if(n.parent() == null) continue;
      final Updates updates = qc.updates();
      final DBNode dbn = updates.determineDataRef(n, qc);
      updates.add(new DeleteNode(dbn.pre(), dbn.data(), info), qc);
    }
    return null;
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new Delete(sc, info, exprs[0].copy(cc, vm));
  }

  @Override
  public String toString() {
    return DELETE + ' ' + NODES + ' ' + exprs[0];
  }
}
