package org.basex.query.func.xquery;

import static org.basex.query.QueryError.BXXQ_UNEXPECTED_X;
import static org.basex.query.QueryError.ZEROFUNCS_X_X;

import java.util.concurrent.ForkJoinPool;

import org.basex.core.jobs.JobException;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.FItem;
import org.basex.query.value.item.Item;
import org.basex.query.value.seq.Empty;
import org.basex.util.Util;

/**
 * Function implementation.
 *
 * @author James Wright
 */
public final class XQueryForkJoin extends StandardFunc {
  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final Value funcs = qc.value(exprs[0]);
    for(final Item func : funcs) {
      if(!(func instanceof FItem) || ((FItem) func).arity() != 0)
        throw ZEROFUNCS_X_X.get(info, func.type, func);
    }
    // no functions specified: return empty sequence
    if(funcs.isEmpty()) return Empty.SEQ;
    // single function: invoke directly
    if(funcs.size() == 1) return ((FItem) funcs.itemAt(0)).invokeValue(qc, info);

    final ForkJoinPool pool = new ForkJoinPool();
    final ForkJoinTask task = new ForkJoinTask(funcs, qc, info);
    try {
      return pool.invoke(task);
    } catch(final Exception ex) {
      // pass on query and job exceptions
      final Throwable e = Util.rootException(ex);
      if(e instanceof QueryException) throw (QueryException) e;
      if(e instanceof JobException) throw (JobException) e;
      throw BXXQ_UNEXPECTED_X.get(info, e);
    } finally {
      // required?
      pool.shutdown();
    }
  }

  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }
}
