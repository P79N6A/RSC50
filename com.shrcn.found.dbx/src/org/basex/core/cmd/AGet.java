package org.basex.core.cmd;

import org.basex.core.Command;
import org.basex.core.locks.LockResult;
import org.basex.core.users.Perm;

/**
 * Abstract class for option commands.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
abstract class AGet extends Command {
  /**
   * Default constructor.
   * @param args arguments
   */
  AGet(final String... args) {
    super(Perm.NONE, args);
  }

  @Override
  public void databases(final LockResult lr) {
    // no locks needed
  }
}
