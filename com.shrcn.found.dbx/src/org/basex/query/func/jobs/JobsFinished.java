package org.basex.query.func.jobs;

import org.basex.core.jobs.JobPool;
import org.basex.core.jobs.JobResult;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Bln;
import org.basex.util.InputInfo;
import org.basex.util.Token;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class JobsFinished extends StandardFunc {
  @Override
  public Bln item(final QueryContext qc, final InputInfo ii) throws QueryException {
    checkAdmin(qc);

    final String id = Token.string(toToken(exprs[0], qc));
    final JobPool pool = qc.context.jobs;
    final JobResult result = pool.results.get(id);
    // returns true if job is unknown and if no result exists, or it has been finished
    return Bln.get(!pool.active.containsKey(id) && (result == null || result.cached()));
  }
}
