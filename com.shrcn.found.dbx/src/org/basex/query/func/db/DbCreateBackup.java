package org.basex.query.func.db;

import static org.basex.query.QueryError.BXDB_NAME_X;
import static org.basex.query.QueryError.BXDB_WHICH_X;
import static org.basex.util.Token.string;

import org.basex.core.Databases;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.up.primitives.name.BackupCreate;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbCreateBackup extends DbAccess {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final String name = string(toToken(exprs[0], qc));
    if(!Databases.validName(name)) throw BXDB_NAME_X.get(info, name);
    if(!qc.context.soptions.dbExists(name)) throw BXDB_WHICH_X.get(info, name);

    qc.updates().add(new BackupCreate(name, info, qc), qc);
    return null;
  }
}
