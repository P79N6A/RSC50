package org.basex.query.func.jobs;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.Token;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class JobsStop extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    checkAdmin(qc);

    final String id = Token.string(toToken(exprs[0], qc));
    org.basex.core.cmd.JobsStop.stop(qc.context, id);
    return null;
  }
}
