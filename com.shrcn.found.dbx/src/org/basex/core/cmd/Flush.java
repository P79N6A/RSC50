package org.basex.core.cmd;

import static org.basex.core.Text.DB_FLUSHED_X;

import org.basex.core.Command;
import org.basex.core.MainOptions;
import org.basex.core.locks.DBLocking;
import org.basex.core.locks.LockResult;
import org.basex.core.users.Perm;
import org.basex.data.Data;

/**
 * Evaluates the 'flush' command and flushes the database buffers.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Flush extends Command {
  /**
   * Default constructor.
   */
  public Flush() {
    super(Perm.WRITE, true);
  }

  @Override
  protected boolean run() {
    final Data data = context.data();
    if(!options.get(MainOptions.AUTOFLUSH)) data.flush(true);
    return info(DB_FLUSHED_X, data.meta.name, job().performance);
  }

  @Override
  public void databases(final LockResult lr) {
    lr.write.add(DBLocking.CONTEXT);
  }
}
