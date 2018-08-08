package org.basex.query.func.jobs;

import static org.basex.query.QueryError.JOBS_RUNNING_X;
import static org.basex.query.QueryError.JOBS_UNKNOWN_X;

import java.util.Map;

import org.basex.core.jobs.JobPool;
import org.basex.core.jobs.JobResult;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.util.Token;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class JobsResult extends StandardFunc {
  @Override
  public Value value(final QueryContext qc) throws QueryException {
    checkAdmin(qc);

    final String id = Token.string(toToken(exprs[0], qc));
    final JobPool jobs = qc.context.jobs;

    final Map<String, JobResult> results = jobs.results;
    final JobResult result = results.get(id);
    if(result == null) throw JOBS_UNKNOWN_X.get(info, id);
    if(result.value == null && result.exception == null) throw JOBS_RUNNING_X.get(info, id);

    try {
      if(result.value == null) throw result.exception;
      return result.value;
    } finally {
      results.remove(id);
    }
  }

  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }
}
