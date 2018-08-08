package org.basex.query.func.hof;

import java.util.Arrays;
import java.util.Comparator;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryRTException;
import org.basex.query.iter.Iter;
import org.basex.query.util.list.ItemList;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public final class HofSortWith extends HofFn {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final Value v = exprs[0].value(qc);
    final Comparator<Item> cmp = getComp(1, qc);
    if(v.size() < 2) return v;
    final ItemList items = v.cache();
    try {
      Arrays.sort(items.internal(), 0, items.size(), cmp);
    } catch(final QueryRTException ex) {
      throw ex.getCause();
    }
    return items.value();
  }
}
