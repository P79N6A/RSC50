package org.basex.query.func.user;

import static org.basex.query.QueryError.USER_ADMIN;

import java.util.ArrayList;

import org.basex.core.users.Perm;
import org.basex.core.users.User;
import org.basex.core.users.UserText;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.up.primitives.UpdateType;
import org.basex.query.up.primitives.UserPermUpdate;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.list.StringList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class UserCreate extends UserFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    checkAdmin(qc);
    final String name = toSafeName(0, qc);
    final String pw = toString(1, qc);
    final ArrayList<Perm> perms = toPerms(2, qc);
    final StringList patterns = toPatterns(3, qc);

    final User user = new User(name, pw);
    if(name.equals(UserText.ADMIN)) throw USER_ADMIN.get(info);

    qc.updates().add(new Create(user, perms, patterns, qc, info), qc);
    return null;
  }

  /** Update primitive. */
  private static final class Create extends UserPermUpdate {
    /**
     * Constructor.
     * @param user user
     * @param perms permissions
     * @param patterns database patterns
     * @param qc query context
     * @param info input info
     * @throws QueryException query exception
     */
    private Create(final User user, final ArrayList<Perm> perms, final StringList patterns,
        final QueryContext qc, final InputInfo info) throws QueryException {
      super(UpdateType.USERCREATE, user, perms, patterns, qc, info);
    }

    @Override
    public void apply() {
      final User olduser = users.get(user.name());
      if(olduser != null) users.drop(olduser);
      users.add(user);
      grant();
    }

    @Override
    public String operation() { return "created"; }
  }
}
