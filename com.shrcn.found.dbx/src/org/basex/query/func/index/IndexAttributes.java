package org.basex.query.func.index;

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
public final class IndexAttributes extends IndexTexts {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return values(qc, IndexType.ATTRIBUTE);
  }
}
