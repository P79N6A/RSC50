package org.basex.query.func.array;

import java.util.Iterator;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
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
public final class ArrayForEachPair extends ArrayFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Array array1 = toArray(exprs[0], qc), array2 = toArray(exprs[1], qc);
    final FItem fun = checkArity(exprs[2], 2, qc);
    final ArrayBuilder builder = new ArrayBuilder();
    final Iterator<Value> as = array1.iterator(0), bs = array2.iterator(0);
    while(as.hasNext() && bs.hasNext())
      builder.append(fun.invokeValue(qc, info, as.next(), bs.next()));
    return builder.freeze();
  }
}
