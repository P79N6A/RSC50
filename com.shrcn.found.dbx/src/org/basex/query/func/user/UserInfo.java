package org.basex.query.func.user;

import org.basex.query.QueryContext;
import org.basex.query.value.node.ANode;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class UserInfo extends UserFn {
  @Override
  public ANode item(final QueryContext qc, final InputInfo ii) {
    return qc.context.users.info();
  }
}
