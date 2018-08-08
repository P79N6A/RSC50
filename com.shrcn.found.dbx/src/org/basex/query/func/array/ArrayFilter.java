package org.basex.query.func.array;

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
public final class ArrayFilter extends ArrayFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Array array = toArray(exprs[0], qc);
    final FItem fun = checkArity(exprs[1], 1, qc);
    final ArrayBuilder builder = new ArrayBuilder();
    for(final Value val : array.members()) {
      if(toBoolean(fun.invokeItem(qc, info, val))) builder.append(val);
    }
    return builder.freeze();
  }
}
