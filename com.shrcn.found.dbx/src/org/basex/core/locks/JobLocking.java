package org.basex.core.locks;

import java.util.LinkedList;

import org.basex.core.Context;
import org.basex.core.StaticOptions;
import org.basex.core.jobs.Job;
import org.basex.util.Util;

/**
 * Management of executing read/write jobs.
 * Supports multiple readers, limited by {@link StaticOptions#PARALLEL},
 * and a single writer (readers/writer lock).
 *
 * Since Version 7.6, this locking class has been replaced by {@link DBLocking}.
 * It can still be activated by setting {@link StaticOptions#GLOBALLOCK} to {@code true}.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class JobLocking implements Locking {
  /** Queue for all waiting jobs. */
  private final LinkedList<Object> queue = new LinkedList<>();
  /** Mutex object. */
  private final Object mutex = new Object();

  /** Number of active readers. */
  private int readers;
  /** Writer flag. */
  private boolean writer;

  @Override
  public void acquire(final Job job, final Context ctx) {
    final Object o = new Object();

    final int parallel = Math.max(ctx.soptions.get(StaticOptions.PARALLEL), 1);
    synchronized(mutex) {
      // add object to queue
      queue.add(o);

      while(true) {
        if(!writer && o == queue.get(0)) {
          if(job.updating) {
            // check updating job
            if(readers == 0) {
              // start writing job
              writer = true;
              break;
            }
          } else if(readers < parallel) {
            // increase number of readers
            ++readers;
            break;
          }
        }
        // check if job has already been stopped
        job.checkStop();
        // wait for next job to be finalized
        try {
          mutex.wait();
        } catch(final InterruptedException ex) {
          Util.stack(ex);
        }
      }
      // start job, remove from queue
      queue.remove(0);
    }
  }

  @Override
  public void release(final Job job) {
    synchronized(mutex) {
      if(job.updating) {
        writer = false;
      } else {
        --readers;
      }
      mutex.notifyAll();
    }
  }
}
