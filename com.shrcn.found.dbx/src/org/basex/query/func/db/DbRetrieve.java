package org.basex.query.func.db;

import static org.basex.query.QueryError.BXDB_MEM_X;
import static org.basex.query.QueryError.IOERR_X;
import static org.basex.query.QueryError.WHICHRES_X;

import org.basex.data.Data;
import org.basex.io.IOFile;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.B64Stream;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbRetrieve extends DbAccess {
  @Override
  public B64Stream item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Data data = checkData(qc);
    final String path = path(1, qc);
    if(data.inMemory()) throw BXDB_MEM_X.get(info, data.meta.name);

    final IOFile file = data.meta.binary(path);
    if(file == null || !file.exists() || file.isDir()) throw WHICHRES_X.get(info, path);
    return new B64Stream(file, IOERR_X);
  }
}
