package org.basex.query.func.array;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.value.array.Array;
import org.basex.query.value.array.ArrayBuilder;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ArrayJoin extends ArrayFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Iter ir = qc.iter(exprs[0]);
    Item it = ir.next();
    if(it == null) return Array.empty();
    final Array fst = toArray(it);
    it = ir.next();
    if(it == null) return fst;
    final Array snd = toArray(it);
    it = ir.next();
    if(it == null) return fst.concat(snd);

    final ArrayBuilder builder = new ArrayBuilder().append(fst).append(snd);
    do {
      builder.append(toArray(it));
    } while((it = ir.next()) != null);
    return builder.freeze();
  }
}
