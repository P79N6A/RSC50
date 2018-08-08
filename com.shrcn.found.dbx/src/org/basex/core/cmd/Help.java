package org.basex.core.cmd;

import static org.basex.core.Text.TRY_SPECIFIC_X;
import static org.basex.core.Text.UNKNOWN_CMD_X;

import java.io.IOException;

import org.basex.core.Command;
import org.basex.core.locks.LockResult;
import org.basex.core.parse.Commands.Cmd;
import org.basex.core.users.Perm;

/**
 * Evaluates the 'help' command and returns help on the database commands.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Help extends Command {
  /**
   * Default constructor.
   * @param arg argument (can be {@code null})
   */
  public Help(final String arg) {
    super(Perm.NONE, arg == null ? "" : arg);
  }

  @Override
  protected boolean run() throws IOException {
    final String key = args[0];
    if(key.isEmpty()) {
      out.println(TRY_SPECIFIC_X);
      for(final Cmd cmd : Cmd.values()) out.print(cmd.help(false));
    } else {
      final Cmd cmd = getOption(key, Cmd.class);
      if(cmd == null) return error(UNKNOWN_CMD_X, this);
      out.println(cmd.help(true));
    }
    return true;
  }

  @Override
  public void databases(final LockResult lr) {
    // no locks needed
  }
}
