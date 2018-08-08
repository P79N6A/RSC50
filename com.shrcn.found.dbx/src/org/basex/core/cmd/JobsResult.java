package org.basex.core.cmd;

import static org.basex.query.QueryError.JOBS_RUNNING_X;
import static org.basex.query.QueryError.JOBS_UNKNOWN_X;

import java.io.IOException;
import java.util.Map;

import org.basex.core.Command;
import org.basex.core.jobs.JobPool;
import org.basex.core.jobs.JobResult;
import org.basex.core.locks.LockResult;
import org.basex.core.parse.CmdBuilder;
import org.basex.core.parse.Commands.Cmd;
import org.basex.core.parse.Commands.CmdJobs;
import org.basex.core.users.Perm;
import org.basex.io.serial.Serializer;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Item;
import org.basex.util.Util;

/**
 * Evaluates the 'jobs stop' command.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class JobsResult extends Command {
  /**
   * Default constructor.
   * @param id id
   */
  public JobsResult(final String id) {
    super(Perm.ADMIN, id);
  }

  @Override
  protected boolean run() {
    final String id = args[0];
    final JobPool jobs = context.jobs;
    final Map<String, JobResult> results = jobs.results;
    final JobResult result = results.get(id);
    if(result == null) return error(JOBS_UNKNOWN_X.desc, id);
    if(!result.cached()) error(JOBS_RUNNING_X.desc, id);

    try {
      if(result.value == null) throw result.exception;

      final Serializer ser = Serializer.get(out);
      final Iter ir = result.value.iter();
      for(Item it; (it = ir.next()) != null;) {
        ser.serialize(it);
        checkStop();
      }
      return true;
    } catch(final QueryException | IOException ex) {
      exception = ex;
      return error(Util.message(ex));
    } finally {
      results.remove(id);
    }
  }

  @Override
  public void databases(final LockResult lr) {
    // no locks needed
  }

  @Override
  public void build(final CmdBuilder cb) {
    cb.init(Cmd.JOBS + " " + CmdJobs.RESULT).args();
  }
}
