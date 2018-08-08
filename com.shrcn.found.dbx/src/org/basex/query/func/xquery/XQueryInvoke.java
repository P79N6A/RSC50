package org.basex.query.func.xquery;

import static org.basex.query.QueryError.IOERR_X;

import java.io.IOException;

import org.basex.io.IO;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.util.list.ItemList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class XQueryInvoke extends XQueryEval {
  @Override
  protected ItemList eval(final QueryContext qc) throws QueryException {
    checkCreate(qc);
    final IO io = checkPath(0, qc);
    try {
      return eval(qc, io.read(), io.path(), false);
    } catch(final IOException ex) {
      throw IOERR_X.get(info, ex);
    }
  }
}
