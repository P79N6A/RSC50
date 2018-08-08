package org.basex.query.func.random;

import static org.basex.query.QueryError.BXRA_BOUNDS_X;

import java.util.Random;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Dirk Kirsten
 */
public final class RandomInteger extends StandardFunc {
  /** Random instance. */
  private static final Random RND = new Random();

  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    if(exprs.length == 0) return Int.get(RND.nextInt());
    final long max = toLong(exprs[0], qc);
    if(max > 0 && max <= Integer.MAX_VALUE) return Int.get(RND.nextInt((int) max));
    throw BXRA_BOUNDS_X.get(info, max);
  }
}
