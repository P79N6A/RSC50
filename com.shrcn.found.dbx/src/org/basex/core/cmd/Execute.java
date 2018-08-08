package org.basex.core.cmd;

import static org.basex.core.Text.NO_DB_OPENED;

import java.util.ArrayList;
import java.util.Collections;

import org.basex.core.Command;
import org.basex.core.Context;
import org.basex.core.locks.LockResult;
import org.basex.core.parse.CmdBuilder;
import org.basex.core.parse.CommandParser;
import org.basex.core.users.Perm;
import org.basex.query.QueryException;
import org.basex.util.Util;

/**
 * Evaluates the 'execute' command and runs a command script.
 * This command can be used to run multiple commands as a single transaction.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class Execute extends Command {
  /** Commands to execute. */
  final ArrayList<Command> list = new ArrayList<>();
  /** Error message. */
  String error;

  /**
   * Default constructor.
   * @param input user input
   */
  public Execute(final String input) {
    super(Perm.ADMIN, false, input);
  }

  @Override
  public final boolean newData(final Context ctx) {
    return close(ctx);
  }

  @Override
  public final void databases(final LockResult lr) {
    lr.writeAll = true;
  }

  @Override
  protected boolean run() {
    try {
      if(!init(context)) return error(error);

      final StringBuilder sb = new StringBuilder();
      for(final Command c : list) {
        if(c.openDB && context.data() == null) return error(NO_DB_OPENED);
        try {
          final boolean ok = pushJob(c).run(context, out);
          sb.append(c.info());
          if(!ok) {
            exception = c.exception();
            return error(sb.toString());
          }
        } finally {
          popJob();
        }
      }
      return info(sb.toString().replaceAll("\r?\n?$", ""));
    } finally {
      finish(context);
    }
  }

  @Override
  public final boolean updating(final Context ctx) {
    if(!init(ctx)) return true;
    for(final Command c : list) {
      if(!c.updating(ctx)) return true;
    }
    return false;
  }

  /**
   * Initializes the specified input.
   * @param ctx database context
   * @return success flag
   */
  boolean init(final Context ctx) {
    return init(args[0], ctx);
  }

  /**
   * Initializes the specified input.
   * @param input command input
   * @param ctx database context
   * @return success flag
   */
  final boolean init(final String input, final Context ctx) {
    if(list.isEmpty() && error == null) {
      try {
        Collections.addAll(list, new CommandParser(input, ctx).parse());
      } catch(final QueryException ex) {
        error = Util.message(ex);
        return false;
      }
    }
    return error == null;
  }

  /**
   * Finalizes command execution.
   * @param ctx database context
   */
  @SuppressWarnings("unused")
  void finish(final Context ctx) {
  }

  @Override
  public void build(final CmdBuilder cb) {
    cb.init().arg(0);
  }
}
