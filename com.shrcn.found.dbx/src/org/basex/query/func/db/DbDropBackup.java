package org.basex.query.func.db;

import static org.basex.query.QueryError.BXDB_NAME_X;
import static org.basex.query.QueryError.BXDB_WHICHBACK_X;
import static org.basex.util.Token.string;

import org.basex.core.Databases;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.up.Updates;
import org.basex.query.up.primitives.name.BackupDrop;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.list.StringList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbDropBackup extends DbAccess {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final String name = string(toToken(exprs[0], qc));
    if(!Databases.validName(name)) throw BXDB_NAME_X.get(info, name);

    final StringList backups = qc.context.databases.backups(name);
    if(backups.isEmpty()) throw BXDB_WHICHBACK_X.get(info, name);

    final Updates updates = qc.updates();
    for(final String backup : backups) updates.add(new BackupDrop(backup, info, qc), qc);
    return null;
  }
}
