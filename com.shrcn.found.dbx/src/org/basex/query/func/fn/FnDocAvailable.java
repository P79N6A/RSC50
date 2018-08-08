package org.basex.query.func.fn;

import org.basex.query.QueryContext;
import org.basex.query.QueryError;
import org.basex.query.QueryError.ErrType;
import org.basex.query.QueryException;
import org.basex.query.value.item.Bln;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.Strings;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnDocAvailable extends Docs {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    try {
      return Bln.get(doc(qc) != null);
    } catch(final QueryException ex) {
      final QueryError error = ex.error();
      if(error != null && error.code.matches("^.*\\d+$")) {
        final int num = Strings.toInt(error.code.replaceAll("^.*(\\d+)$", "$1"));
        if(error.is(ErrType.FODC) && (num == 2 || num == 4 || num == 5) ||
           error.is(ErrType.BXDB) && num == 6) return Bln.FALSE;
      }
      throw ex;
    }
  }
}
