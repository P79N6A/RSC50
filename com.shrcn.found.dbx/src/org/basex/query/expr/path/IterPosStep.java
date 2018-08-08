package org.basex.query.expr.path;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Arr;
import org.basex.query.expr.Expr;
import org.basex.query.expr.Pos;
import org.basex.query.iter.BasicNodeIter;
import org.basex.query.iter.NodeIter;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.ANode;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.ft.Scoring;
import org.basex.util.hash.IntObjMap;

/**
 * Iterative step expression with one or more simple numeric predicates.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class IterPosStep extends Step {
  /**
   * Constructor.
   * @param info input info
   * @param axis axis
   * @param test node test
   * @param preds predicates
   */
  IterPosStep(final InputInfo info, final Axis axis, final Test test, final Expr... preds) {
    super(info, axis, test, preds);
  }

  @Override
  public NodeIter iter(final QueryContext qc) {
    return new NodeIter() {
      final Pos[] posExpr = new Pos[preds.length];
      final long[] cPos = new long[preds.length];
      boolean skip;
      BasicNodeIter iter;

      @Override
      public ANode next() throws QueryException {
        if(skip) return null;
        if(iter == null) {
          iter = axis.iter(checkNode(qc));
          final int pl = preds.length;
          for(int p = 0; p < pl; p++) {
            final Expr pred = preds[p];
            if(pred instanceof Pos) {
              posExpr[p] = (Pos) pred;
            } else if(num(pred)) {
              // pre-evaluate numeric position
              final Item it = pred.atomItem(qc, info);
              if(it == null) return null;
              final double dbl = toDouble(it);
              final long lng = (long) dbl;
              if(dbl != lng) return null;
              final Expr e = Pos.get(lng, info);
              if(e instanceof Pos) posExpr[p] = (Pos) e;
              else return null;
            }
          }
        }

        for(final ANode node : iter) {
          qc.checkStop();
          if(test.eq(node) && preds(node)) return node.finish();
        }
        return null;
      }

      /**
       * Evaluates the predicates.
       * @param node input node
       * @return result of check
       * @throws QueryException query exception
       */
      private boolean preds(final ANode node) throws QueryException {
        final Value cv = qc.value;
        qc.value = node;
        try {
          double s = qc.scoring ? 0 : -1;
          final int pl = preds.length;
          for(int p = 0; p < pl; p++) {
            final Expr pred = preds[p];
            final Pos pos = posExpr[p];
            if(pos == null) {
              final Item tst = pred.test(qc, info);
              if(tst == null) return false;
              if(s != -1) s += tst.score();
            } else {
              final long ps = ++cPos[p];
              if(!pos.matches(ps)) return false;
              if(pos.skip(ps)) skip = true;
            }
          }
          if(s > 0) node.score(Scoring.avg(s, preds.length));
        } finally {
          qc.value = cv;
        }
        return true;
      }
    };
  }

  @Override
  public Step copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return copyType(new IterPosStep(info, axis, test.copy(), Arr.copyAll(cc, vm, preds)));
  }
}
