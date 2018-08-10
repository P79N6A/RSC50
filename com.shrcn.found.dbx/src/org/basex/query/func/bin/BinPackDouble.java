package org.basex.query.func.bin;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
public final class BinPackDouble extends BinFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final double d = toDouble(exprs[0], qc);
    final ByteOrder bo = order(1, qc);
    return new B64(ByteBuffer.wrap(new byte[8]).order(bo).putDouble(d).array());
  }
}
