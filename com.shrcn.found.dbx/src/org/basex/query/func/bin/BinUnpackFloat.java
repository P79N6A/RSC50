package org.basex.query.func.bin;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Flt;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class BinUnpackFloat extends BinFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return Flt.get(unpack(qc, 4).getFloat());
  }
}
