package org.basex.query.up.primitives.name;

import static org.basex.query.QueryError.BXDB_ONCEBACK_X_X;
import static org.basex.query.QueryError.UPDROPBACK_X_X;

import org.basex.core.cmd.DropBackup;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.Function;
import org.basex.query.up.primitives.Update;
import org.basex.query.up.primitives.UpdateType;
import org.basex.util.InputInfo;

/**
 * Update primitive for the {@link Function#_DB_DROP_BACKUP} function.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class BackupDrop extends NameUpdate {
  /**
   * Constructor.
   * @param name name of backup file to be dropped
   * @param info input info
   * @param qc query context
   */
  public BackupDrop(final String name, final InputInfo info, final QueryContext qc) {
    super(UpdateType.BACKUPDROP, name, info, qc);
  }

  @Override
  public void merge(final Update update) throws QueryException {
    throw BXDB_ONCEBACK_X_X.get(info, name, operation());
  }

  @Override
  public void apply() throws QueryException {
    if(!DropBackup.drop(name, qc.context.soptions))
      throw UPDROPBACK_X_X.get(info, name, operation());
  }

  @Override
  public void prepare() { }

  @Override
  protected String operation() { return "dropped"; }
}
