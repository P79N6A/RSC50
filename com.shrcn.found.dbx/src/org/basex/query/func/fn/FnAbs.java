package org.basex.query.func.fn;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.ANum;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnAbs extends StandardFunc {
  @Override
  public ANum item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final ANum num = toNumber(exprs[0], qc);
    return num == null ? null : num.abs();
  }
}
