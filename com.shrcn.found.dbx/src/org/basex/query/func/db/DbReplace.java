package org.basex.query.func.db;

import static org.basex.query.QueryError.BXDB_REPLACE_X;
import static org.basex.util.Token.token;

import org.basex.data.Data;
import org.basex.io.IOFile;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.up.Updates;
import org.basex.query.up.primitives.NewInput;
import org.basex.query.up.primitives.db.DBAdd;
import org.basex.query.up.primitives.db.DBDelete;
import org.basex.query.up.primitives.db.DBStore;
import org.basex.query.up.primitives.node.DeleteNode;
import org.basex.query.up.primitives.node.ReplaceDoc;
import org.basex.query.value.item.Bin;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.list.IntList;
import org.basex.util.options.Options;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbReplace extends DbNew {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Data data = checkData(qc);
    final String path = path(1, qc);
    final Item item = toNodeOrAtomItem(exprs[2], qc);
    final Options opts = toOptions(3, new Options(), qc);

    final Updates updates = qc.updates();
    final IntList docs = data.resources.docs(path);
    int d = 0;

    // delete binary resources
    final IOFile bin = data.meta.binary(path);
    final boolean disk = !data.inMemory();
    if(disk && (bin == null || bin.isDir())) throw BXDB_REPLACE_X.get(info, path);

    if(disk && item instanceof Bin) {
      updates.add(new DBStore(data, path, item, info), qc);
    } else {
      if(disk && bin.exists()) updates.add(new DBDelete(data, path, info), qc);
      final NewInput input = checkInput(item, token(path));
      if(docs.isEmpty()) {
        updates.add(new DBAdd(data, input, opts, true, qc, info), qc);
      } else {
        updates.add(new ReplaceDoc(docs.get(d++), data, input, opts, qc, info), qc);
      }
    }

    // delete old documents
    final int ds = docs.size();
    for(; d < ds; d++) updates.add(new DeleteNode(docs.get(d), data, info), qc);
    return null;
  }
}
