package org.basex.query.func.repo;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.util.pkg.RepoManager;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.Token;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class RepoDelete extends RepoFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    checkCreate(qc);
    new RepoManager(qc.context, info).delete(Token.string(toToken(exprs[0], qc)));
    return null;
  }
}
