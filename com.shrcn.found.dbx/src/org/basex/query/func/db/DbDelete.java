package org.basex.query.func.db;

import static org.basex.query.QueryError.BXDB_PATH_X;

import org.basex.data.Data;
import org.basex.io.IOFile;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.up.Updates;
import org.basex.query.up.primitives.db.DBDelete;
import org.basex.query.up.primitives.node.DeleteNode;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.list.IntList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbDelete extends DbAccess {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Data data = checkData(qc);
    final String path = path(1, qc);

    // delete XML resources
    final IntList docs = data.resources.docs(path);
    final int is = docs.size();
    final Updates updates = qc.updates();
    for(int i = 0; i < is; i++) {
      updates.add(new DeleteNode(docs.get(i), data, info), qc);
    }
    // delete raw resources
    if(!data.inMemory()) {
      final IOFile bin = data.meta.binary(path);
      if(bin == null) throw BXDB_PATH_X.get(info, path);
      updates.add(new DBDelete(data, path, info), qc);
    }
    return null;
  }
}
