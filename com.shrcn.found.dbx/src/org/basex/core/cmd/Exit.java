package org.basex.core.cmd;

import org.basex.core.Command;
import org.basex.core.locks.DBLocking;
import org.basex.core.locks.LockResult;
import org.basex.core.users.Perm;

/**
 * Evaluates the 'exit' command and quits the console.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Exit extends Command {
  /** Constructor. */
  public Exit() {
    super(Perm.NONE);
  }

  @Override
  protected boolean run() {
    return close(context);
  }

  @Override
  public void databases(final LockResult lr) {
    lr.read.add(DBLocking.CONTEXT);
  }
}
