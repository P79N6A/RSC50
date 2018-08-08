package org.basex.query.func.db;

import org.basex.index.IndexType;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbToken extends DbText {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return attribute(valueAccess(IndexType.TOKEN, qc), qc, 2);
  }
}
