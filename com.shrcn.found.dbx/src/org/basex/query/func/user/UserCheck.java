package org.basex.query.func.user;

import static org.basex.query.QueryError.USER_PASSWORD_X;

import org.basex.core.users.User;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.Token;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class UserCheck extends UserFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final User user = toUser(0, qc);
    if(user.matches(Token.string(toToken(exprs[1], qc)))) return null;
    throw USER_PASSWORD_X.get(info, user.name());
  }
}
