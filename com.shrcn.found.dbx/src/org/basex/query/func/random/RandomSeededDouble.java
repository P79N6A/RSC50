package org.basex.query.func.random;

import static org.basex.query.QueryError.BXRA_NUM_X;

import java.util.Random;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Dbl;
import org.basex.query.value.item.Item;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Dirk Kirsten
 */
public final class RandomSeededDouble extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final long seed = toLong(exprs[0], qc);
    final long num = toLong(exprs[1], qc);
    if(num < 0) throw BXRA_NUM_X.get(info, num);

    return new Iter() {
      final Random r = new Random(seed);
      long c;

      @Override
      public Item next() {
        return ++c <= num ? Dbl.get(r.nextDouble()) : null;
      }
    };
  }
}
