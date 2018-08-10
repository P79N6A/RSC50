package org.basex.query.func.bin;

import static org.basex.query.QueryError.BIN_NNC;
import static org.basex.util.Token.ZERO;
import static org.basex.util.Token.concat;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.B64;
import org.basex.query.value.item.Hex;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class BinHex extends BinFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    byte[] bytes = str(0, qc);
    if(bytes == null) return null;

    // add leading zero
    if((bytes.length & 1) != 0) bytes = concat(ZERO, bytes);
    try {
      return new B64(Hex.decode(bytes, info));
    } catch(final QueryException ex) {
      throw BIN_NNC.get(info);
    }
  }
}
