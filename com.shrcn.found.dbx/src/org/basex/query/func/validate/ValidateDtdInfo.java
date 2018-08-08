package org.basex.query.func.validate;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.Value;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ValidateDtdInfo extends ValidateDtd {
  @Override
  public Value value(final QueryContext qc) throws QueryException {
    return info(qc);
  }
}
