package org.basex.query.func.ft;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FtExtract extends FtMark {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return mark(qc, true);
  }
}
