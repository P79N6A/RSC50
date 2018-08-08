package org.basex.core.cmd;

import static org.basex.core.Text.JOBS_STOPPED_X;

import java.util.TimerTask;

import org.basex.core.Command;
import org.basex.core.Context;
import org.basex.core.jobs.Job;
import org.basex.core.locks.LockResult;
import org.basex.core.parse.CmdBuilder;
import org.basex.core.parse.Commands.Cmd;
import org.basex.core.parse.Commands.CmdJobs;
import org.basex.core.users.Perm;

/**
 * Evaluates the 'jobs stop' command.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class JobsStop extends Command {
  /**
   * Default constructor.
   * @param id id
   */
  public JobsStop(final String id) {
    super(Perm.ADMIN, id);
  }

  @Override
  protected boolean run() {
    final boolean stopped = stop(context, args[0]);
    return info(JOBS_STOPPED_X, stopped ? 1 : 0);
  }

  /**
   * Stops the specified process.
   * @param ctx context
   * @param id id
   * @return return success flag
   */
  public static boolean stop(final Context ctx, final String id) {
    // stop scheduled task
    final TimerTask task = ctx.jobs.tasks.remove(id);
    if(task != null) task.cancel();
    // send stop signal to job
    final Job job = ctx.jobs.active.get(id);
    if(job != null) job.stop();
    // remove potentially cached result
    ctx.jobs.results.remove(id);

    return job != null || task != null;
  }

  @Override
  public void databases(final LockResult lr) {
    // no locks needed
  }

  @Override
  public void build(final CmdBuilder cb) {
    cb.init(Cmd.JOBS + " " + CmdJobs.STOP).args();
  }
}
