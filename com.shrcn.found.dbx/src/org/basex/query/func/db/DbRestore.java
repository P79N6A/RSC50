package org.basex.query.func.db;

import static org.basex.query.QueryError.BXDB_NAME_X;
import static org.basex.query.QueryError.BXDB_NOBACKUP_X;
import static org.basex.util.Token.string;

import org.basex.core.Databases;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.up.primitives.name.DBRestore;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.list.StringList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbRestore extends DbAccess {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    // extract database name from backup file
    final String name = string(toToken(exprs[0], qc));
    if(!Databases.validName(name)) throw BXDB_NAME_X.get(info, name);

    // find backup with or without date suffix
    final StringList backups = qc.context.databases.backups(name);
    if(backups.isEmpty()) throw BXDB_NOBACKUP_X.get(info, name);

    final String backup = backups.get(0);
    final String db = Databases.name(backup);
    qc.updates().add(new DBRestore(db, backup, qc, info), qc);
    return null;
  }
}
