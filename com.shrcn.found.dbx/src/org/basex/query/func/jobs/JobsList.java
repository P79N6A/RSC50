package org.basex.query.func.jobs;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.seq.StrSeq;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class JobsList extends StandardFunc {
  @Override
  public Value value(final QueryContext qc) throws QueryException {
    checkAdmin(qc);
    return StrSeq.get(org.basex.core.cmd.JobsList.ids(qc.context));
  }

  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }
}
