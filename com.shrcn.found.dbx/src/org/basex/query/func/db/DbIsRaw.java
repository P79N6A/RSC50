package org.basex.query.func.db;

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
public final class DbIsRaw extends DbAccess {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Data data = checkData(qc);
    final String path = path(1, qc);
    if(data.inMemory()) return Bln.FALSE;
    final IOFile io = data.meta.binary(path);
    return Bln.get(io.exists() && !io.isDir());
  }
}
