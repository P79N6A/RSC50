package org.basex.query.func.db;

import static org.basex.query.QueryError.BXDB_MEM_X;
import static org.basex.query.QueryError.RESINV_X;

import org.basex.data.Data;
import org.basex.io.IOFile;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.up.primitives.db.DBStore;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbStore extends DbAccess {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Data data = checkData(qc);
    final String path = path(1, qc);
    final Item item = toNodeOrAtomItem(exprs[2], qc);
    if(data.inMemory()) throw BXDB_MEM_X.get(info, data.meta.name);

    final IOFile file = data.meta.binary(path);
    if(file == null || path.isEmpty()) throw RESINV_X.get(info, path);
    qc.updates().add(new DBStore(data, path, item, info), qc);
    return null;
  }
}
