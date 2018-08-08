package org.basex.core.cmd;

import static org.basex.core.Text.DB_CLOSED_X;

import org.basex.core.Command;
import org.basex.core.Context;
import org.basex.core.locks.DBLocking;
import org.basex.core.locks.LockResult;
import org.basex.core.users.Perm;
import org.basex.data.Data;

/**
 * Evaluates the 'close' command and closes the current database.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Close extends Command {
  /**
   * Default constructor.
   */
  public Close() {
    super(Perm.NONE);
  }

  @Override
  protected boolean run() {
    final Data data = context.data();
    if(data == null) return true;

    close(data, context);
    context.closeDB();
    return info(DB_CLOSED_X, data.meta.name);
  }

  @Override
  public void databases(final LockResult lr) {
    lr.read.add(DBLocking.CONTEXT);
  }

  /**
   * Closes the specified database.
   * @param data data reference
   * @param ctx database context
   */
  public static void close(final Data data, final Context ctx) {
    synchronized(ctx.datas) { ctx.datas.unpin(data); }
  }
}
