package org.basex.query.func.array;

import static org.basex.query.QueryError.ARRAYEMPTY;

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
public final class ArrayTail extends ArrayFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Array array = toArray(exprs[0], qc);
    if(array.isEmptyArray()) throw ARRAYEMPTY.get(info);
    return array.tail();
  }
}
