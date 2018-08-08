package org.basex.query.func.convert;

import static org.basex.query.QueryError.BXCO_INVBASEDIG_X_X;
import static org.basex.query.QueryError.BXCO_INVBASE_X;

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
 * @author Christian Gruen
 */
public final class ConvertIntegerFromBase extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] str = toToken(exprs[0], qc);
    final long base = toLong(exprs[1], qc);
    if(base < 2 || base > 36) throw BXCO_INVBASE_X.get(info, base);

    long res = 0;
    for(final byte b : str) {
      final int num = b <= '9' ? b - 0x30 : (b & 0xDF) - 0x37;
      if(!(b >= '0' && b <= '9' || b >= 'a' && b <= 'z' || b >= 'A' && b <= 'Z') || num >= base)
        throw BXCO_INVBASEDIG_X_X.get(info, base, (char) (b & 0xff));

      res = res * base + num;
    }
    return Int.get(res);
  }
}
