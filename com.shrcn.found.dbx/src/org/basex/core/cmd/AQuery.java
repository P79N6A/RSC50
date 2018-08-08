package org.basex.core.cmd;

import static org.basex.core.Text.COL;
import static org.basex.core.Text.ERROR;
import static org.basex.core.Text.INTERRUPTED;
import static org.basex.core.Text.NL;
import static org.basex.core.Text.QUERY_PLAN;
import static org.basex.query.QueryError.BASX_STACKOVERFLOW;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.basex.core.Command;
import org.basex.core.Context;
import org.basex.core.MainOptions;
import org.basex.core.StaticOptions;
import org.basex.core.jobs.JobException;
import org.basex.core.locks.LockResult;
import org.basex.core.parse.CmdBuilder;
import org.basex.core.users.Perm;
import org.basex.io.IOFile;
import org.basex.io.out.BufferOutput;
import org.basex.io.out.NullOutput;
import org.basex.io.out.PrintOutput;
import org.basex.io.serial.Serializer;
import org.basex.io.serial.SerializerMode;
import org.basex.io.serial.dot.DOTSerializer;
import org.basex.query.QueryException;
import org.basex.query.QueryInfo;
import org.basex.query.QueryProcessor;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.util.Performance;
import org.basex.util.Util;

/**
 * Abstract class for database queries.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public abstract class AQuery extends Command {
  /** Variables. */
  protected final HashMap<String, String[]> vars = new HashMap<>();

  /** HTTP context. */
  private Object http;
  /** Query processor. */
  private QueryProcessor qp;
  /** Query info. */
  private QueryInfo info;
  /** Query result. */
  private Value result;

  /**
   * Protected constructor.
   * @param perm required permission
   * @param openDB requires opened database
   * @param args arguments
   */
  AQuery(final Perm perm, final boolean openDB, final String... args) {
    super(perm, openDB, args);
  }

  /**
   * Evaluates the specified query.
   * @param query query
   * @return success flag
   */
  final boolean query(final String query) {
    final Performance p = new Performance();
    String error;
    if(exception != null) {
      error = Util.message(exception);
    } else {
      try {
        long hits = 0;
        final boolean run = options.get(MainOptions.RUNQUERY);
        final boolean serial = options.get(MainOptions.SERIALIZE);
        final int runs = Math.max(1, options.get(MainOptions.RUNS));
        for(int r = 0; r < runs; ++r) {
          // reuse existing processor instance
          if(r != 0) {
            qp = null;
            popJob();
          }
          qp(query, context);
          parse(p);
          if(r == 0) plan(false);

          qp.compile();
          info.compiling += p.time();
          if(r == 0) plan(true);
          if(!run) continue;

          final PrintOutput po = r == 0 && serial ? out : new NullOutput();
          try(final Serializer ser = qp.getSerializer(po)) {
            if(maxResults >= 0) {
              result = qp.cache(maxResults);
              info.evaluating += p.time();
              result.serialize(ser);
              hits = result.size();
            } else {
              hits = 0;
              final Iter ir = qp.iter();
              info.evaluating += p.time();
              for(Item it; (it = ir.next()) != null;) {
                ser.serialize(it);
                ++hits;
                checkStop();
              }
            }
          }
          qp.close();
          info.serializing += p.time();
        }
        // remove string list if global locking is used and if query is updating
        if(soptions.get(StaticOptions.GLOBALLOCK) && qp.updating) {
          info.readLocked = null;
          info.writeLocked = null;
        }
        return info(info.toString(qp, out.size(), hits, options.get(MainOptions.QUERYINFO)));

      } catch(final QueryException | IOException ex) {
        exception = ex;
        error = Util.message(ex);
      } catch(final JobException ex) {
        error = INTERRUPTED;
      } catch(final StackOverflowError ex) {
        Util.debug(ex);
        error = BASX_STACKOVERFLOW.desc;
      } catch(final RuntimeException ex) {
        extError("");
        Util.debug(info());
        throw ex;
      } finally {
        // close processor after exceptions
        if(qp != null) qp.close();
      }
    }
    return extError(error);
  }

  /**
   * Parses the query.
   * @param p performance
   * @throws QueryException query exception
   */
  private void parse(final Performance p) throws QueryException {
    qp.http(http);
    for(final Entry<String, String[]> entry : vars.entrySet()) {
      final String name = entry.getKey();
      final String[] value = entry.getValue();
      if(name == null) qp.context(value[0], value[1]);
      else qp.bind(name, value[0], value[1]);
    }
    qp.parse();
    if(p != null) info.parsing += p.time();
  }

  /**
   * Checks if the query possibly performs updates.
   * @param ctx database context
   * @param query query string
   * @return result of check
   */
  final boolean updates(final Context ctx, final String query) {
    try {
      final Performance p = new Performance();
      qp(query, ctx);
      parse(p);
      return qp.updating;
    } catch(final QueryException ex) {
      Util.debug(ex);
      exception = ex;
      qp.close();
      return false;
    }
  }

  /**
   * Returns a query processor instance.
   * @param query query string
   * @param ctx database context
   * @return query processor
   */
  private QueryProcessor qp(final String query, final Context ctx) {
    if(qp == null) {
      qp = pushJob(new QueryProcessor(query, ctx));
      if(info == null) info = qp.qc.info;
    }
    return qp;
  }

  /**
   * Returns the serialization parameters.
   * @param ctx context
   * @return serialization parameters
   */
  public final String parameters(final Context ctx) {
    try {
      qp(args[0], ctx);
      parse(null);
      return qp.qc.serParams().toString();
    } catch(final QueryException ex) {
      error(Util.message(ex));
    } finally {
      qp = null;
      popJob();
    }
    return SerializerMode.DEFAULT.get().toString();
  }

  /**
   * Binds the HTTP context.
   * @param value HTTP context
   */
  public final void http(final Object value) {
    http = value;
  }

  /**
   * Returns an extended error message.
   * @param err error message
   * @return result of check
   */
  private boolean extError(final String err) {
    // will only be evaluated when an error has occurred
    final StringBuilder sb = new StringBuilder();
    if(options.get(MainOptions.QUERYINFO)) {
      sb.append(info()).append(qp.info()).append(NL).append(ERROR).append(COL).append(NL);
    }
    sb.append(err);
    return error(sb.toString());
  }

  /**
   * Creates query plans.
   * @param comp compiled flag
   */
  private void plan(final boolean comp) {
    if(comp != options.get(MainOptions.COMPPLAN)) return;

    // show dot plan
    try {
      if(options.get(MainOptions.DOTPLAN)) {
        final String path = options.get(MainOptions.QUERYPATH);
        final String dot = path.isEmpty() ? "plan.dot" :
            new IOFile(path).name().replaceAll("\\..*?$", ".dot");

        try(final BufferOutput bo = new BufferOutput(dot)) {
          try(final DOTSerializer d = new DOTSerializer(bo, options.get(MainOptions.DOTCOMPACT))) {
            d.serialize(qp.plan());
          }
        }
      }

      // show XML plan
      if(options.get(MainOptions.XMLPLAN)) {
        info(NL + QUERY_PLAN + COL);
        info(qp.plan().serialize().toString());
      }
    } catch(final Exception ex) {
      Util.stack(ex);
    }
  }

  @Override
  public boolean updating(final Context ctx) {
    return updates(ctx, args[0]);
  }

  @Override
  public final boolean updated(final Context ctx) {
    return qp != null && qp.updates() != 0;
  }

  @Override
  public void databases(final LockResult lr) {
    if(qp == null) {
      lr.writeAll = true;
    } else {
      qp.databases(lr);
      info.readLocked = lr.readAll ? null : lr.read;
      info.writeLocked = lr.writeAll ? null : lr.write;
    }
  }

  @Override
  public void build(final CmdBuilder cb) {
    cb.init().xquery(0);
  }

  @Override
  public boolean stoppable() {
    return true;
  }

  @Override
  public final Value result() {
    final Value r = result;
    result = null;
    return r;
  }
}
