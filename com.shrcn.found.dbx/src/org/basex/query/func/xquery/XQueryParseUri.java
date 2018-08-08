package org.basex.query.func.xquery;

import static org.basex.query.QueryError.IOERR_X;

import java.io.IOException;

import org.basex.io.IO;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.node.FElem;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class XQueryParseUri extends XQueryParse {
  @Override
  public FElem item(final QueryContext qc, final InputInfo ii) throws QueryException {
    checkCreate(qc);
    final IO io = checkPath(0, qc);
    try {
      return parse(qc, io.read(), io.path());
    } catch(final IOException ex) {
      throw IOERR_X.get(info, ex);
    }
  }
}
