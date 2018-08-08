package org.basex.query.func.fn;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnOutermost extends Nodes {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return most(qc, true);
  }
}
