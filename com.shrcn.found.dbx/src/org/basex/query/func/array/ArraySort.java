package org.basex.query.func.array;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.func.fn.FnSort;
import org.basex.query.util.list.ValueList;
import org.basex.query.value.Value;
import org.basex.query.value.array.Array;
import org.basex.query.value.array.ArrayBuilder;
import org.basex.query.value.item.FItem;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ArraySort extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Array array = toArray(exprs[0], qc);

    final int sz = (int) array.arraySize();
    final ValueList vl = new ValueList(sz);
    final ArrayBuilder builder = new ArrayBuilder();
    if(exprs.length > 1) {
      final FItem key = checkArity(exprs[1], 1, qc);
      for(final Value value : array.members()) vl.add(key.invokeValue(qc, info, value));
    } else {
      for(final Value value : array.members()) vl.add(value.atomValue(qc, info));
    }
    for(final int order : FnSort.sort(vl, this)) builder.append(array.get(order));
    return builder.freeze();
  }
}
