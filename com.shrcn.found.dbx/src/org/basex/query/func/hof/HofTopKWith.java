package org.basex.query.func.hof;

import java.util.Comparator;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryRTException;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.Item;
import org.basex.query.value.seq.Empty;
import org.basex.util.MinHeap;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public final class HofTopKWith extends HofFn {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final Comparator<Item> cmp = getComp(1, qc);
    final long k = Math.min(toLong(exprs[2], qc), Integer.MAX_VALUE);
    if(k < 1) return Empty.SEQ;

    final Iter iter = exprs[0].iter(qc);
    final MinHeap<Item, Item> heap = new MinHeap<>(cmp);

    try {
      for(Item it; (it = iter.next()) != null;) {
        heap.insert(it, it);
        if(heap.size() > k) heap.removeMin();
      }
    } catch(final QueryRTException ex) { throw ex.getCause(); }

    final ValueBuilder vb = new ValueBuilder();
    while(!heap.isEmpty()) vb.addFront(heap.removeMin());
    return vb.value();
  }
}
