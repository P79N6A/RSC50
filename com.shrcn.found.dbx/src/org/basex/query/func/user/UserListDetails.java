package org.basex.query.func.user;

import static org.basex.core.users.UserText.ALGORITHM;
import static org.basex.core.users.UserText.DATABASE;
import static org.basex.core.users.UserText.NAME;
import static org.basex.core.users.UserText.PASSWORD;
import static org.basex.core.users.UserText.PATTERN;
import static org.basex.core.users.UserText.PERMISSION;
import static org.basex.core.users.UserText.USER;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map.Entry;

import org.basex.core.Context;
import org.basex.core.users.Algorithm;
import org.basex.core.users.Code;
import org.basex.core.users.Perm;
import org.basex.core.users.User;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.node.FElem;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class UserListDetails extends UserList {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final Context ctx = qc.context;
    final User u = exprs.length > 0 ? toUser(0, qc) : null;
    final ValueBuilder vb = new ValueBuilder();
    for(final User us : u != null ? Collections.singletonList(u) : ctx.users.users(null, ctx)) {
      final String perm = us.perm((String) null).toString();
      final FElem user = new FElem(USER).add(NAME, us.name()).add(PERMISSION, perm);
      for(final Entry<Algorithm, EnumMap<Code, String>> codes : us.alg().entrySet()) {
        final FElem password = new FElem(PASSWORD).add(ALGORITHM, codes.getKey().toString());
        for(final Entry<Code, String> code : codes.getValue().entrySet()) {
          password.add(new FElem(code.getKey().toString()).add(code.getValue()));
        }
        user.add(password);
      }
      for(final Entry<String, Perm> local : us.locals().entrySet()) {
        user.add(new FElem(DATABASE).add(PATTERN, local.getKey()).
            add(PERMISSION, local.getValue().toString()));
      }
      vb.add(user);
    }
    return vb.value();
  }
}
