package org.basex.core.cmd;

import static org.basex.core.Text.ADMIN_STATIC;
import static org.basex.core.Text.NAME_INVALID_X;
import static org.basex.core.Text.UNKNOWN_USER_X;
import static org.basex.core.Text.USER_EXISTS_X;
import static org.basex.core.Text.USER_RENAMED_X_X;

import org.basex.core.Databases;
import org.basex.core.parse.CmdBuilder;
import org.basex.core.parse.Commands.Cmd;
import org.basex.core.parse.Commands.CmdAlter;
import org.basex.core.users.User;
import org.basex.core.users.UserText;
import org.basex.core.users.Users;
import org.basex.util.Strings;

/**
 * Evaluates the 'alter user' command and alters the name of a user.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class AlterUser extends AUser {
  /**
   * Default constructor.
   * @param name user name
   * @param newname new name
   */
  public AlterUser(final String name, final String newname) {
    super(name, newname);
  }

  @Override
  protected boolean run() {
    final String name = args[0], newname = args[1];
    if(!Databases.validName(name)) return error(NAME_INVALID_X, name);
    if(!Databases.validName(newname)) return error(NAME_INVALID_X, newname);
    if(Strings.eq(UserText.ADMIN, name, newname)) return error(ADMIN_STATIC);

    final Users users = context.users;
    final User user = users.get(name);
    if(user == null) return error(UNKNOWN_USER_X, name);
    if(users.get(newname) != null) return error(USER_EXISTS_X, newname);

    users.alter(user, newname);
    users.write(context);
    return info(USER_RENAMED_X_X, name, newname);
  }

  @Override
  public void build(final CmdBuilder cb) {
    cb.init(Cmd.ALTER + " " + CmdAlter.USER).arg(0);
    if(!cb.conf()) cb.arg(1);
  }
}
