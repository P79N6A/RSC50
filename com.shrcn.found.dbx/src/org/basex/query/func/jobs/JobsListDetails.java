package org.basex.query.func.jobs;

import static org.basex.util.Token.chop;
import static org.basex.util.Token.normalize;
import static org.basex.util.Token.string;
import static org.basex.util.Token.token;

import java.util.Locale;

import org.basex.core.Context;
import org.basex.core.StaticOptions;
import org.basex.core.cmd.JobsList;
import org.basex.core.jobs.Job;
import org.basex.core.jobs.JobContext;
import org.basex.core.jobs.JobPool;
import org.basex.core.jobs.JobResult;
import org.basex.core.jobs.JobTask;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.DTDur;
import org.basex.query.value.item.Dtm;
import org.basex.query.value.node.FElem;
import org.basex.util.list.TokenList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class JobsListDetails extends StandardFunc {
  /** Job. */
  private static final byte[] JOB = token("job");
  /** ID. */
  private static final byte[] ID = token("id");
  /** Running. */
  private static final byte[] DURATION = token("duration");
  /** Type. */
  private static final byte[] TYPE = token("type");
  /** State. */
  private static final byte[] STATE = token("state");
  /** Next start. */
  private static final byte[] START = token("start");
  /** End. */
  private static final byte[] END = token("end");
  /** User. */
  private static final byte[] USER = token("user");

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    checkAdmin(qc);

    final Context ctx = qc.context;
    final TokenList ids = exprs.length == 0 ? JobsList.ids(ctx) :
      new TokenList(1).add(toToken(exprs[0], qc));

    final JobPool jobs = ctx.jobs;
    final int ml = ctx.soptions.get(StaticOptions.LOGMSGMAXLEN);
    final ValueBuilder vb = new ValueBuilder();
    for(final byte[] key : ids) {
      final String id = string(key);
      Job job = jobs.active.get(id);
      final JobTask jt = jobs.tasks.get(id);
      final JobResult jr = jobs.results.get(id);
      if(job == null && jr != null) job = jr.job;
      if(job == null && jt != null) job = jt.job;
      if(job == null) continue;

      final JobContext jc = job.job();
      final long ms = jc.performance != null
          ? (System.nanoTime() - jc.performance.start()) / 1000000 : jr != null
          ? jr.time / 1000000 : -1;

      final FElem elem = new FElem(JOB);
      elem.add(ID, id);
      elem.add(TYPE, jc.type());
      elem.add(STATE, job.state.toString().toLowerCase(Locale.ENGLISH));
      elem.add(USER, jc.context.user().name());
      if(ms >= 0) elem.add(DURATION, DTDur.get(ms).string(info));
      if(jt != null) {
        elem.add(START, Dtm.get(jt.start).string(info));
        if(jt.end != Long.MAX_VALUE) elem.add(END, Dtm.get(jt.end).string(info));
      }
      elem.add(chop(normalize(token(jc.toString())), ml));
      vb.add(elem);
    }
    return vb.value();
  }

  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }
}
