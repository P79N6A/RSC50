package org.basex.core.cmd;

import static org.basex.core.Text.NAME_INVALID_X;
import static org.basex.core.Text.ON;

import java.io.IOException;

import org.basex.core.Command;
import org.basex.core.Databases;
import org.basex.core.locks.DBLocking;
import org.basex.core.locks.LockResult;
import org.basex.core.parse.CmdBuilder;
import org.basex.core.parse.Commands.Cmd;
import org.basex.core.parse.Commands.CmdShow;
import org.basex.core.users.Perm;

/**
 * Evaluates the 'show users' command and shows existing users.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ShowUsers extends Command {
  /**
   * Default constructor.
   */
  public ShowUsers() {
    this(null);
  }

  /**
   * Constructor, specifying a database.
   * @param db database (can be {@code null})
   */
  public ShowUsers(final String db) {
    super(Perm.NONE, db == null ? "" : db);
  }

  @Override
  protected boolean run() throws IOException {
    final String name = args[0];
    if(!name.isEmpty() && !Databases.validName(name)) return error(NAME_INVALID_X, name);
    out.println(context.users.info(name.isEmpty() ? null : name, context).finish());
    return true;
  }

  @Override
  public void databases(final LockResult lr) {
    lr.read.add(DBLocking.ADMIN);
  }

  @Override
  public void build(final CmdBuilder cb) {
    cb.init(Cmd.SHOW + " " + CmdShow.USERS).arg(ON, 0);
  }
}
