package org.basex.query.up.primitives.name;

import static org.basex.query.QueryError.UPDBOPTERR_X;

import java.io.IOException;

import org.basex.core.cmd.CreateBackup;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.Function;
import org.basex.query.up.primitives.UpdateType;
import org.basex.util.InputInfo;

/**
 * Update primitive for the {@link Function#_DB_CREATE_BACKUP} function.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Lukas Kircher
 */
public final class BackupCreate extends NameUpdate {
  /**
   * Constructor.
   * @param name name of database to be backed up
   * @param info input info
   * @param qc query context
   */
  public BackupCreate(final String name, final InputInfo info, final QueryContext qc) {
    super(UpdateType.BACKUPCREATE, name, info, qc);
  }

  @Override
  public void apply() throws QueryException {
    try {
      CreateBackup.backup(name, qc.context.soptions, null);
    } catch(final IOException ex) {
      throw UPDBOPTERR_X.get(info, ex);
    }
  }

  @Override
  public void prepare() { }

  @Override
  public String operation() { return "backed up"; }
}
