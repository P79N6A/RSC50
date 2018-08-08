package org.basex.query.func.user;

import java.util.ArrayList;

import org.basex.core.users.User;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.seq.StrSeq;
import org.basex.util.list.TokenList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class UserList extends UserFn {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final ArrayList<User> users = qc.context.users.users(null, qc.context);
    final TokenList tl = new TokenList(users.size());
    for(final User user : users) tl.add(user.name());
    return StrSeq.get(tl);
  }
}
