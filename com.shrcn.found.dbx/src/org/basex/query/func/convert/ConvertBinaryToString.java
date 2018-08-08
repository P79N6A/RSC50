package org.basex.query.func.convert;

import static org.basex.query.QueryError.BXCO_ENCODING_X;
import static org.basex.query.QueryError.BXCO_STRING_X;

import java.io.IOException;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Bin;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ConvertBinaryToString extends ConvertFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Bin bin = toBin(exprs[0], qc);
    final String enc = toEncoding(1, BXCO_ENCODING_X, qc);
    final boolean val = exprs.length < 3 || !toBoolean(exprs[2], qc);
    try {
      return Str.get(toString(bin.input(info), enc, val));
    } catch(final IOException ex) {
      throw BXCO_STRING_X.get(info, ex);
    }
  }
}
