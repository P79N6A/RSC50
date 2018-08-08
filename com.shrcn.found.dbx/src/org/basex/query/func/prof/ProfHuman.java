package org.basex.query.func.prof;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;
import org.basex.util.Performance;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ProfHuman extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return Str.get(Performance.format(toLong(exprs[0], qc), true));
  }
}
