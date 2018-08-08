package org.basex.query.expr.path;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Arr;
import org.basex.query.expr.Expr;
import org.basex.query.iter.NodeIter;
import org.basex.query.value.node.ANode;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * Iterative step expression with a single last() predicate.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class IterLastStep extends Step {
  /**
   * Constructor.
   * @param info input info
   * @param axis axis
   * @param test node test
   * @param preds predicates
   */
  IterLastStep(final InputInfo info, final Axis axis, final Test test, final Expr... preds) {
    super(info, axis, test, preds);
  }

  @Override
  public NodeIter iter(final QueryContext qc) {
    return new NodeIter() {
      boolean stop;

      @Override
      public ANode next() throws QueryException {
        if(stop) return null;
        stop = true;

        // return last items
        ANode litem = null;
        final Test tst = test;
        for(final ANode node : axis.iter(checkNode(qc))) {
          qc.checkStop();
          if(tst.eq(node)) litem = node.finish();
        }
        return litem == null ? null : litem;
      }
    };
  }

  @Override
  public IterLastStep copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return copyType(new IterLastStep(info, axis, test.copy(), Arr.copyAll(cc, vm, preds)));
  }
}
