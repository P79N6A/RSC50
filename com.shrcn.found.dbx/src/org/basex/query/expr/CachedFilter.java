package org.basex.query.expr;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.util.list.ItemList;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.FElem;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * Filter expression, caching all results.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class CachedFilter extends Filter {
  /**
   * Constructor.
   * @param info input info
   * @param root root expression
   * @param preds predicates
   */
  public CachedFilter(final InputInfo info, final Expr root, final Expr... preds) {
    super(info, root, preds);
  }

  @Override
  public final Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    Value val = root.value(qc);
    final Value cv = qc.value;
    final long cs = qc.size, cp = qc.pos;

    try {
      // evaluate first predicate, based on incoming value
      final ItemList buffer = new ItemList();
      Expr pred = preds[0];
      long vs = val.size();
      qc.size = vs;
      qc.pos = 1;

      final boolean scoring = qc.scoring;
      for(int s = 0; s < vs; s++) {
        final Item item = val.itemAt(s);
        qc.value = item;
        final Item test = pred.test(qc, info);
        if(test != null) {
          if(scoring) item.score(test.score());
          buffer.add(item);
        }
        qc.pos++;
      }
      // save memory
      val = null;

      // evaluate remaining predicates, based on value builder
      final int pl = preds.length;
      for(int i = 1; i < pl; i++) {
        vs = buffer.size();
        pred = preds[i];
        qc.size = vs;
        qc.pos = 1;
        int c = 0;
        for(int s = 0; s < vs; ++s) {
          final Item item = buffer.get(s);
          qc.value = item;
          final Item test = pred.test(qc, info);
          if(test != null) {
            if(scoring) item.score(test.score());
            buffer.set(c++, item);
          }
          qc.pos++;
        }
        buffer.size(c);
      }

      // return resulting values
      return buffer.value();
    } finally {
      qc.value = cv;
      qc.size = cs;
      qc.pos = cp;
    }
  }

  @Override
  public Filter copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return copyType(new CachedFilter(info, root.copy(cc, vm), Arr.copyAll(cc, vm, preds)));
  }

  @Override
  public final void plan(final FElem plan) {
    final FElem el = planElem();
    addPlan(plan, el, root);
    super.plan(el);
  }
}
