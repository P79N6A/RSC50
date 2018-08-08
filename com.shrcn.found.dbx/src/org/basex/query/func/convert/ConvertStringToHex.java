package org.basex.query.func.convert;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Hex;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ConvertStringToHex extends ConvertFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return new Hex(stringToBinary(qc));
  }
}
