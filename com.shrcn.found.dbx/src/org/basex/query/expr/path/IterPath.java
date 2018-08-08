package org.basex.query.expr.path;

import static org.basex.query.QueryError.PATHNODE_X_X_X;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Arr;
import org.basex.query.expr.Expr;
import org.basex.query.iter.Iter;
import org.basex.query.iter.NodeIter;
import org.basex.query.util.list.ExprList;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.ANode;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * Iterative path expression for location paths which return sorted and
 * duplicate-free results.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class IterPath extends AxisPath {
  /**
   * Constructor.
   * @param info input info
   * @param root root expression
   * @param steps axis steps
   */
  IterPath(final InputInfo info, final Expr root, final Expr... steps) {
    super(info, root, steps);
  }

  @Override
  protected NodeIter nodeIter(final QueryContext qc) {
    return new NodeIter() {
      final boolean r = root != null;
      final int sz = steps.length + (r ? 1 : 0);
      final Expr[] expr = r ? new ExprList(sz).add(root).add(steps).finish() : steps;
      final Iter[] iter = new Iter[sz];
      ANode node;
      int pos = -1;

      @Override
      public ANode next() throws QueryException {
        // local copy of variables (faster)
        ANode n = node;
        int p = pos;
        if(p == -1) {
          ++p;
          iter[p] = qc.iter(expr[p]);
        }

        final Value cv = qc.value;
        final long cp = qc.pos, cs = qc.size;
        try {
          while(true) {
            final Item it = iter[p].next();
            if(it == null) {
              iter[p] = null;
              if(--p == -1) {
                n = null;
                break;
              }
            } else if(p < sz - 1) {
              // ensure that root only returns nodes
              if(r && p == 0 && !(it instanceof ANode))
                throw PATHNODE_X_X_X.get(info, steps[0], it.type, it);
              qc.value = it;
              ++p;
              iter[p] = qc.iter(expr[p]);
            } else {
              // remaining steps will always yield nodes
              final ANode nx = (ANode) it;
              if(n == null || !n.is(nx)) {
                n = nx;
                break;
              }
            }
          }
          pos = p;
          node = n;
          return n;
        } finally {
          // reset context and return result
          qc.value = cv;
          qc.pos = cp;
          qc.size = cs;
        }
      }
    };
  }

  @Override
  public IterPath copy(final CompileContext cc, final IntObjMap<Var> vm) {
    final Expr rt = root == null ? null : root.copy(cc, vm);
    return copyType(new IterPath(info, rt,  Arr.copyAll(cc, vm, steps)));
  }
}
