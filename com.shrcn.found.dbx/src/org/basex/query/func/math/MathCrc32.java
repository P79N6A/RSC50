package org.basex.query.func.math;

import java.util.zip.CRC32;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Hex;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class MathCrc32 extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final CRC32 crc = new CRC32();
    crc.update(toToken(exprs[0], qc));
    final byte[] r = new byte[4];
    for(int i = r.length, c = (int) crc.getValue(); i-- > 0; c >>>= 8) r[i] = (byte) c;
    return new Hex(r);
  }
}
