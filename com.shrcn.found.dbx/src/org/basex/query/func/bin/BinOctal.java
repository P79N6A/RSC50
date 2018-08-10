package org.basex.query.func.bin;

import static org.basex.query.QueryError.BIN_NNC;
import static org.basex.util.Token.EMPTY;
import static org.basex.util.Token.string;
import static org.basex.util.Token.token;

import java.math.BigInteger;
import java.util.Arrays;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.B64;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class BinOctal extends BinFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] bytes = str(0, qc);
    if(bytes == null) return null;
    final int bl = bytes.length;
    if(bl == 0) return new B64(EMPTY);

    try {
      byte[] bin = token(new BigInteger(string(bytes), 8).toString(2));
      final int expl = bl * 3;
      final int binl = bin.length;
      if(binl != expl) {
        // add leading zeroes
        final byte[] tmp = new byte[expl];
        Arrays.fill(tmp, 0, expl - binl, (byte) '0');
        System.arraycopy(bin, 0, tmp, expl - binl, binl);
        bin = tmp;
      }
      return new B64(binary2bytes(bin));
    } catch(final NumberFormatException ex) {
      throw BIN_NNC.get(info);
    }
  }
}
