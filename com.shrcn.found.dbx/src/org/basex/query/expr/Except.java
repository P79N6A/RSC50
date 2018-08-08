package org.basex.query.expr;

import static org.basex.query.QueryText.OPTREMOVE_X_X;

import org.basex.query.CompileContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.iter.NodeIter;
import org.basex.query.util.list.ANodeList;
import org.basex.query.util.list.ExprList;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.ANode;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * Except expression.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Except extends Set {
  /**
   * Constructor.
   * @param info input info
   * @param exprs expressions
   */
  public Except(final InputInfo info, final Expr[] exprs) {
    super(info, exprs);
  }

  @Override
  public Expr optimize(final CompileContext cc) throws QueryException {
    super.optimize(cc);

    final ExprList el = new ExprList(exprs.length);
    for(final Expr ex : exprs) {
      if(ex.isEmpty()) {
        // remove empty operands (return empty sequence if first value is empty)
        if(el.isEmpty()) return optPre(cc);
        cc.info(OPTREMOVE_X_X, this, ex);
      } else {
        el.add(ex);
      }
    }
    // ensure that results are always sorted
    if(el.size() == 1 && iterable) return el.get(0);
    // replace expressions with optimized list
    exprs = el.finish();
    return this;
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    final Except ex = new Except(info, copyAll(cc, vm, exprs));
    ex.iterable = iterable;
    return copyType(ex);
  }

  @Override
  protected ANodeList eval(final Iter[] iter) throws QueryException {
    final ANodeList list = new ANodeList().check();

    for(Item it; (it = iter[0].next()) != null;) list.add(toNode(it));
    final boolean db = list.dbnodes();

    final int el = exprs.length;
    for(int e = 1; e < el && !list.isEmpty(); e++) {
      final Iter ir = iter[e];
      for(Item it; (it = ir.next()) != null;) {
        final int i = list.indexOf(toNode(it), db);
        if(i != -1) list.delete(i);
      }
    }
    return list;
  }

  @Override
  protected NodeIter iter(final Iter[] iter) {
    return new SetIter(iter) {
      @Override
      public ANode next() throws QueryException {
        if(item == null) {
          final int il = iter.length;
          item = new ANode[il];
          for(int i = 0; i < il; i++) next(i);
        }

        final int il = item.length;
        for(int i = 1; i < il; i++) {
          if(item[0] == null) return null;
          if(item[i] == null) continue;
          final int d = item[0].diff(item[i]);

          if(d < 0 && i + 1 == il) break;
          if(d == 0) {
            next(0);
            i = 0;
          }
          if(d > 0) next(i--);
        }
        final ANode temp = item[0];
        next(0);
        return temp;
      }
    };
  }
}
