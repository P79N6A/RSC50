package org.basex.query.func.file;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.fn.Parse;
import org.basex.query.iter.Iter;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FileReadTextLines extends FileRead {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    checkCreate(qc);
    return Parse.textIter(text(qc).string(info));
  }
}
