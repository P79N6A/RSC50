package org.basex.query.func.bin;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.B64;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class BinNot extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final B64 b64 = toB64(exprs[0], qc, true);
    if(b64 == null) return null;

    final byte[] bytes = b64.binary(info);
    final int bl = bytes.length;

    final byte[] tmp = new byte[bl];
    for(int b = 0; b < bl; b++) tmp[b] = (byte) ~bytes[b];
    return new B64(tmp);
  }
}
