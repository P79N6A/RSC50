package org.basex.query.func.fn;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.BasicNodeIter;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnIdref extends Ids {
  @Override
  public BasicNodeIter iter(final QueryContext qc) throws QueryException {
    return ids(qc, true);
  }
}
