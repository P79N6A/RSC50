package org.basex.core.jobs;

import java.util.TimerTask;

import org.basex.query.func.jobs.ScheduledXQuery;

/**
 * Scheduled job.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class JobTask extends TimerTask {
  /** Job. */
  public final ScheduledXQuery job;
  /** Job pool. */
  public final JobPool jobs;
  /** Delay. */
  public final long delay;
  /** Interval. */
  public final long interval;
  /** End time (@link {@link Long#MAX_VALUE}: no end). */
  public final long end;

  /** Next start time. */
  public long start;

  /**
   * Constructor.
   * @param job job
   * @param jobs job pool
   * @param delay delay (ms)
   * @param interval interval (ms)
   * @param duration total duration (ms)
   */
  public JobTask(final ScheduledXQuery job, final JobPool jobs, final long delay,
      final long interval, final long duration) {

    this.job = job;
    this.jobs = jobs;
    this.delay = delay;
    this.interval = interval;
    final long time = System.currentTimeMillis();
    start = time + delay;
    end = duration == Long.MAX_VALUE ? duration : time + duration;

    jobs.tasks.put(job.job().id(), this);
    if(interval > 0) {
      jobs.timer.scheduleAtFixedRate(this, delay, interval);
    } else {
      jobs.timer.schedule(this, delay);
    }
  }

  @Override
  public void run() {
    // skip execution if same job is still running
    final String id = job.job().id();
    if(jobs.active.get(id) == null) new Thread(job).start();

    start += interval;
    if(start >= end || interval == 0) {
      jobs.tasks.remove(id);
      cancel();
    }
  }
}
