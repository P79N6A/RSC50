package org.basex.query.func.convert;

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
public final class ConvertStringToBase64 extends ConvertFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return new B64(stringToBinary(qc));
  }
}
