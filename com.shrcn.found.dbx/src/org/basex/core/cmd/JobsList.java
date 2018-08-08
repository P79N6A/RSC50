package org.basex.core.cmd;

import static org.basex.core.Text.DURATION;
import static org.basex.core.Text.END;
import static org.basex.core.Text.JOBS;
import static org.basex.core.Text.START;
import static org.basex.core.Text.STATE;
import static org.basex.core.Text.TYPE;
import static org.basex.core.Text.USER;
import static org.basex.util.Token.EMPTY;
import static org.basex.util.Token.ID;
import static org.basex.util.Token.string;
import static org.basex.util.Token.uc;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.basex.core.Command;
import org.basex.core.Context;
import org.basex.core.jobs.Job;
import org.basex.core.jobs.JobContext;
import org.basex.core.jobs.JobPool;
import org.basex.core.jobs.JobResult;
import org.basex.core.jobs.JobTask;
import org.basex.core.locks.LockResult;
import org.basex.core.parse.CmdBuilder;
import org.basex.core.parse.Commands.Cmd;
import org.basex.core.parse.Commands.CmdJobs;
import org.basex.core.users.Perm;
import org.basex.query.value.item.DTDur;
import org.basex.query.value.item.Dtm;
import org.basex.util.Table;
import org.basex.util.Token;
import org.basex.util.list.TokenList;

/**
 * Evaluates the 'jobs list' command.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class JobsList extends Command {
  /**
   * Default constructor.
   */
  public JobsList() {
    super(Perm.ADMIN);
  }

  @Override
  protected boolean run() throws IOException {
    final Table table = new Table();
    table.description = JOBS;
    table.header.add(uc(ID));
    table.header.add(TYPE);
    table.header.add(STATE);
    table.header.add(USER);
    table.header.add(DURATION);
    table.header.add(START);
    table.header.add(END);

    final JobPool jobs = context.jobs;
    for(final byte[] key : ids(context)) {
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

      final TokenList tl = new TokenList(5);
      tl.add(id);
      tl.add(jc.type());
      tl.add(job.state.toString().toLowerCase(Locale.ENGLISH));
      tl.add(jc.context.user().name());
      tl.add(ms >= 0 ? DTDur.get(ms).string(null) : EMPTY);
      tl.add(jt != null ? Dtm.get(jt.start).string(null) : EMPTY);
      tl.add(jt != null && jt.end != Long.MAX_VALUE ? Dtm.get(jt.end).string(null) : EMPTY);
      table.contents.add(tl);
    }
    out.println(table.sort().finish());
    return true;
  }

  /**
   * Returns all registered ids.
   * @param ctx database context
   * @return id list
   */
  public static TokenList ids(final Context ctx) {
    final JobPool jobs = ctx.jobs;
    final Set<String> set = new HashSet<>();
    for(final String id : jobs.results.keySet()) set.add(id);
    for(final String id : jobs.active.keySet()) set.add(id);
    for(final String id : jobs.tasks.keySet()) set.add(id);
    final TokenList list = new TokenList(set.size());
    for(final String id : set) list.add(id);
    return sort(list);
  }

  /**
   * Sorts a list of job ids.
   * @param list job id
   * @return sorted list
   */
  private static TokenList sort(final TokenList list) {
    return list.sort(new Comparator<byte[]>() {
      @Override
      public int compare(final byte[] token1, final byte[] token2) {
        final byte[] t1 = Token.substring(token1, 3), t2 = Token.substring(token2, 3);
        final long diff = Token.toLong(t1) - Token.toLong(t2);
        return diff < 0 ? -1 : diff > 0 ? 1 : 0;
      }
    }, true);
  }

  @Override
  public void databases(final LockResult lr) {
    // no locks needed
  }

  @Override
  public void build(final CmdBuilder cb) {
    cb.init(Cmd.JOBS + " " + CmdJobs.LIST).args();
  }
}
