package org.basex.query.func.unit;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class UnitFail extends UnitFn {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    throw error(exprs.length < 1 ? null : toNodeOrAtomItem(exprs[0], qc));
  }
}
