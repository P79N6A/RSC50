package org.basex.query.up.primitives.name;

import static org.basex.query.QueryError.UPDBOPTERR_X;

import java.io.IOException;

import org.basex.core.cmd.Restore;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.Function;
import org.basex.query.up.primitives.UpdateType;
import org.basex.util.InputInfo;

/**
 * Update primitive for the {@link Function#_DB_RESTORE} function.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Lukas Kircher
 */
public final class DBRestore extends NameUpdate {
  /** Backup to restore. */
  private final String backup;

  /**
   * Constructor.
   * @param name database name
   * @param backup backup file
   * @param qc query context
   * @param info input info
   */
  public DBRestore(final String name, final String backup, final QueryContext qc,
      final InputInfo info) {

    super(UpdateType.DBRESTORE, name, info, qc);
    this.backup = backup;
  }

  @Override
  public void apply() throws QueryException {
    close();
    // restore backup
    try {
      Restore.restore(name, backup, qc.context.soptions, null);
    } catch(final IOException ex) {
      throw UPDBOPTERR_X.get(info, ex);
    }
  }

  @Override
  public void prepare() { }

  @Override
  public String operation() { return "restored"; }
}
