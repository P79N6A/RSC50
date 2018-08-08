package org.basex.query.func.fn;

import static org.basex.query.QueryError.INVCODE_X;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;
import org.basex.util.TokenBuilder;
import org.basex.util.XMLToken;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnCodepointsToString extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Iter ir = exprs[0].atomIter(qc, info);
    final TokenBuilder tb = new TokenBuilder(Math.max(8, (int) ir.size()));
    for(Item it; (it = ir.next()) != null;) {
      final long n = toLong(it);
      final int i = (int) n;
      // check int boundaries before casting
      if(n < Integer.MIN_VALUE || n > Integer.MAX_VALUE || !XMLToken.valid(i))
        throw INVCODE_X.get(info, Long.toHexString(n));
      tb.add(i);
    }
    return Str.get(tb.finish());
  }
}
