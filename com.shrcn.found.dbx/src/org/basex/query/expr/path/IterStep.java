
package org.basex.query.expr.path;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Arr;
import org.basex.query.expr.Expr;
import org.basex.query.iter.BasicNodeIter;
import org.basex.query.iter.NodeIter;
import org.basex.query.value.node.ANode;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * Iterative step expression without numeric predicates.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class IterStep extends Step {
  /**
   * Constructor.
   * @param info input info
   * @param axis axis
   * @param test node test
   * @param preds predicates
   */
  IterStep(final InputInfo info, final Axis axis, final Test test, final Expr[] preds) {
    super(info, axis, test, preds);
  }

  @Override
  public NodeIter iter(final QueryContext qc) {
    return new NodeIter() {
      BasicNodeIter iter;

      @Override
      public ANode next() throws QueryException {
        if(iter == null) iter = axis.iter(checkNode(qc));
        for(final ANode node : iter) {
          qc.checkStop();
          if(test.eq(node) && preds(node, qc)) return node.finish();
        }
        return null;
      }
    };
  }

  @Override
  public IterStep copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return copyType(new IterStep(info, axis, test.copy(), Arr.copyAll(cc, vm, preds)));
  }
}
