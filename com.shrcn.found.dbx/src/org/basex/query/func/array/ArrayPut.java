package org.basex.query.func.array;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.array.Array;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ArrayPut extends ArrayFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Array array = toArray(exprs[0], qc);
    return array.put(checkPos(array, toLong(exprs[1], qc), false), qc.value(exprs[2]));
  }
}
