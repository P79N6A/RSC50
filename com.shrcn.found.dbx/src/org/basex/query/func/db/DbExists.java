package org.basex.query.func.db;

import static org.basex.query.QueryError.BXDB_OPEN_X;

import org.basex.data.Data;
import org.basex.io.IOFile;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Bln;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbExists extends DbAccess {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    try {
      final Data data = checkData(qc);
      if(exprs.length == 1) return Bln.TRUE;
      // check if raw file or XML document exists
      final String path = path(1, qc);
      boolean raw = false;
      if(!data.inMemory()) {
        final IOFile io = data.meta.binary(path);
        raw = io.exists() && !io.isDir();
      }
      return Bln.get(raw || data.resources.doc(path) != -1);
    } catch(final QueryException ex) {
      if(ex.error() == BXDB_OPEN_X) return Bln.FALSE;
      throw ex;
    }
  }
}
