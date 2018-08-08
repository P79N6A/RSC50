package org.basex.query.func.fn;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnAvg extends Aggr {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Iter iter = exprs[0].atomIter(qc, info);
    final Item it = iter.next();
    return it == null ? null : sum(iter, it, true);
  }
}
