package org.basex.query.func.hof;

import java.util.Comparator;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryRTException;
import org.basex.query.expr.CmpV.OpV;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.FItem;
import org.basex.query.value.item.Item;
import org.basex.query.value.seq.Empty;
import org.basex.util.MinHeap;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public final class HofTopKBy extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final FItem getKey = checkArity(exprs[1], 1, qc);
    final long k = Math.min(toLong(exprs[2], qc), Integer.MAX_VALUE);
    if(k < 1) return Empty.SEQ;

    final Iter iter = exprs[0].iter(qc);
    final MinHeap<Item, Item> heap = new MinHeap<>(new Comparator<Item>() {
      @Override
      public int compare(final Item it1, final Item it2) {
        try {
          return OpV.LT.eval(it1, it2, sc.collation, sc, info) ? -1 : 1;
        } catch(final QueryException qe) {
          throw new QueryRTException(qe);
        }
      }
    });

    try {
      for(Item it; (it = iter.next()) != null;) {
        heap.insert(checkNoEmpty(getKey.invokeItem(qc, info, it)), it);
        if(heap.size() > k) heap.removeMin();
      }
    } catch(final QueryRTException ex) { throw ex.getCause(); }

    final ValueBuilder vb = new ValueBuilder();
    while(!heap.isEmpty()) vb.addFront(heap.removeMin());
    return vb.value();
  }
}
