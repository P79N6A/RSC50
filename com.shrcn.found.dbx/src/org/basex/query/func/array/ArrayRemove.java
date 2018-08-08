package org.basex.query.func.array;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.value.array.Array;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.list.LongList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ArrayRemove extends ArrayFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    Array array = toArray(exprs[0], qc);

    // collect positions, sort and remove duplicates
    final LongList list = new LongList();
    final Iter pos = exprs[1].iter(qc);
    for(Item it; (it = pos.next()) != null;) {
      list.add(checkPos(array, toLong(it), false));
    }
    list.sort().distinct();

    // delete entries backwards
    for(int i = list.size() - 1; i >= 0; i--) array = array.remove(list.get(i));
    return array;
  }
}
