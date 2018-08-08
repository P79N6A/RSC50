package org.basex.core;

import static org.basex.core.Text.H_OUT_OF_MEM;
import static org.basex.core.Text.INTERRUPTED;
import static org.basex.core.Text.NO_DB_OPENED;
import static org.basex.core.Text.OUT_OF_MEM;
import static org.basex.core.Text.PERM_REQUIRED_X;
import static org.basex.core.Text.UNKNOWN_TRY_X;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import org.basex.core.cmd.AQuery;
import org.basex.core.cmd.Close;
import org.basex.core.jobs.Job;
import org.basex.core.jobs.JobException;
import org.basex.core.parse.CmdBuilder;
import org.basex.core.users.Perm;
import org.basex.data.Data;
import org.basex.io.out.ArrayOutput;
import org.basex.io.out.NullOutput;
import org.basex.io.out.PrintOutput;
import org.basex.query.value.Value;
import org.basex.util.Performance;
import org.basex.util.Prop;
import org.basex.util.TokenBuilder;
import org.basex.util.Util;
import org.basex.util.list.StringList;
import org.xml.sax.InputSource;

/**
 * This class provides the architecture for all internal command
 * implementations. It evaluates queries that are sent by the GUI, the client or
 * the standalone version.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public abstract class Command extends Job {
  /** Command arguments. */
  public final String[] args;
  /** Permission required to execute this command. */
  public final Perm perm;
  /** Indicates if the command requires an opened database. */
  public final boolean openDB;

  /** Database context. */
  protected Context context;
  /** Convenience access to database options. */
  protected MainOptions options;
  /** Convenience access to static options. */
  protected StaticOptions soptions;

  /** Output stream. */
  protected PrintOutput out;
  /** Optional input source. */
  protected InputSource in;

  /** Exception, resulting from command execution. */
  protected Exception exception;
  /** Maximum number of results (ignored if negative). */
  protected int maxResults = -1;

  /** Container for query information. */
  private final TokenBuilder info = new TokenBuilder();

  /**
   * Constructor for commands requiring no opened database.
   * @param perm required permission
   * @param args arguments
   */
  protected Command(final Perm perm, final String... args) {
    this(perm, false, args);
  }

  /**
   * Constructor.
   * @param perm required permission
   * @param openDB requires an opened database
   * @param args arguments
   */
  protected Command(final Perm perm, final boolean openDB, final String... args) {
    this.perm = perm;
    this.openDB = openDB;
    this.args = args;
  }

  /**
   * Executes the command and prints the result to the specified output
   * stream. If an exception occurs, a {@link BaseXException} is thrown.
   * @param ctx database context
   * @param os output stream reference
   * @throws BaseXException command exception
   */
  public final void execute(final Context ctx, final OutputStream os) throws BaseXException {
    // check if data reference is available
    final Data data = ctx.data();
    if(data == null && openDB) throw new BaseXException(NO_DB_OPENED);

    // check permissions
    if(!ctx.perm(perm, data != null && !data.inMemory() ? data.meta.name : null))
      throw new BaseXException(PERM_REQUIRED_X, perm);

    // checks if the command performs updates
    updating = updating(ctx);

    try {
      // register job
      register(ctx);
      // run command and return success flag
      if(!run(ctx, os)) {
        final BaseXException ex = new BaseXException(info());
        ex.initCause(exception);
        throw ex;
      }
    } catch(final RuntimeException th) {
      Util.stack(th);
      throw th;
    } finally {
      // ensure that job will be unregistered
      unregister(ctx);
    }
  }

  /**
   * Executes the command and returns the result as string.
   * If an exception occurs, a {@link BaseXException} is thrown.
   * @param ctx database context
   * @return string result
   * @throws BaseXException command exception
   */
  public final String execute(final Context ctx) throws BaseXException {
    final ArrayOutput ao = new ArrayOutput();
    execute(ctx, ao);
    return ao.toString();
  }

  /**
   * Attaches an input stream.
   * @param input input stream
   */
  public final void setInput(final InputStream input) {
    in = new InputSource(input);
  }

  /**
   * Attaches an input source.
   * @param input input source
   */
  public final void setInput(final InputSource input) {
    in = input;
  }

  /**
   * Runs the command without permission, data and concurrency checks.
   * Should only be called by other database commands.
   * @param ctx database context
   * @return result of check
   */
  public final boolean run(final Context ctx) {
    return run(ctx, new NullOutput());
  }

  /**
   * Returns command information.
   * @return info string
   */
  public final String info() {
    return info.toString();
  }

  /**
   * Returns the cause of an error.
   * @return error
   */
  public final Exception exception() {
    return exception;
  }

  /**
   * Returns a cached result set generated by {@link AQuery#query}. Can only be called once.
   * @return result set
   */
  public Value result() {
    return null;
  }

  /**
   * Checks if the command performs updates/write operations.
   * @param ctx database context
   * @return result of check
   */
  @SuppressWarnings("unused")
  public boolean updating(final Context ctx) {
    return perm == Perm.CREATE || perm == Perm.WRITE;
  }

  /**
   * Checks if the command has updated any data.
   * @param ctx database context
   * @return result of check
   */
  public boolean updated(final Context ctx) {
    return updating(ctx);
  }

  /**
   * Closes an open data reference and returns {@code true} if this command will change the
   * {@link Context#data()} reference. This method is only required by the GUI.
   * @param ctx database context
   * @return result of check
   */
  @SuppressWarnings("unused")
  public boolean newData(final Context ctx) {
    return false;
  }

  /**
   * Enforces a maximum number of query results. This method is only required by the GUI.
   * @param max maximum number of results (ignored if negative)
   */
  public final void maxResults(final int max) {
    maxResults = max;
  }

  /**
   * Returns true if this command returns a progress value.
   * This method is only required by the GUI.
   * @return result of check
   */
  public boolean supportsProg() {
    return false;
  }

  /**
   * Returns true if this command can be stopped.
   * This method is only required by the GUI.
   * @return result of check
   */
  public boolean stoppable() {
    return false;
  }

  /**
   * Initializes the command execution.
   * @param ctx database context
   * @param os output stream
   */
  public final void init(final Context ctx, final OutputStream os) {
    context = ctx;
    options = ctx.options;
    soptions = ctx.soptions;
    out = PrintOutput.get(os);
  }

  /**
   * Runs the command without permission, data and concurrency checks.
   * @param ctx database context
   * @param os output stream
   * @return result of check
   */
  public final boolean run(final Context ctx, final OutputStream os) {
    init(ctx, os);
    try {
      return run();
    } catch(final JobException ex) {
      // job was interrupted by the user or server
      abort();
      return error(INTERRUPTED);
    } catch(final OutOfMemoryError ex) {
      // out of memory
      Performance.gc(2);
      abort();
      Util.debug(ex);
      return error(OUT_OF_MEM + (perm == Perm.CREATE ? H_OUT_OF_MEM : ""));
    } catch(final Throwable ex) {
      // any other unexpected error
      abort();
      return error(Util.bug(ex) + Prop.NL + info);
    } finally {
      // flushes the output
      try {
        if(out != null) out.flush();
      } catch(final IOException ignore) { }
    }
  }

  /**
   * Returns a string representation of the command.
   * @param conf hide confidential information
   * @return result of check
   */
  public final String toString(final boolean conf) {
    final CmdBuilder cb = new CmdBuilder(this, conf);
    build(cb);
    return cb.toString();
  }


  @Override
  public final String toString() {
    return toString(false);
  }

  // PROTECTED METHODS ========================================================

  /**
   * Executes the command and serializes the result (internal call).
   * @return success of operation
   * @throws IOException I/O exception
   */
  protected abstract boolean run() throws IOException;

  /**
   * Builds a string representation from the command. This string must be
   * correctly built, as commands are sent to the server as strings.
   * @param cb command builder
   */
  protected void build(final CmdBuilder cb) {
    cb.init().args();
  }

  /**
   * Adds the error message to the message buffer {@link #info}.
   * @param msg error message
   * @param ext error extension
   * @return {@code false}
   */
  protected final boolean error(final String msg, final Object... ext) {
    info.reset();
    info.addExt(msg == null ? "" : msg, ext);
    return false;
  }

  /**
   * Adds information on command execution.
   * @param str information to be added
   * @param ext extended info
   * @return {@code true}
   */
  protected final boolean info(final String str, final Object... ext) {
    if(!str.isEmpty()) info.addExt(str, ext).add(Prop.NL);
    return true;
  }

  /**
   * Returns the specified command option.
   * @param typ options enumeration
   * @param <E> token type
   * @return option
   */
  protected final <E extends Enum<E>> E getOption(final Class<E> typ) {
    final E e = getOption(args[0], typ);
    if(e == null) error(UNKNOWN_TRY_X, args[0]);
    return e;
  }

  /**
   * Adds the names of the database that has been addressed by the argument index. No databases will
   * be added if the argument uses glob syntax.
   * @param db databases
   * @param index argument index
   * @return {@code false} if database cannot be determined due to glob syntax
   */
  protected final boolean databases(final StringList db, final int index) {
    // return true if the addressed database argument does not exists
    if(args.length <= index || args[index] == null) return true;
    final boolean noglob = !args[index].matches(".*[\\?\\*,].*");
    if(noglob) db.add(args[index]);
    return noglob;
  }

  /**
   * Returns the specified command option.
   * @param string string to be found
   * @param type options enumeration
   * @param <E> token type
   * @return option
   */
  protected static <E extends Enum<E>> E getOption(final String string, final Class<E> type) {
    try {
      return Enum.valueOf(type, string.toUpperCase(Locale.ENGLISH));
    } catch(final Exception ex) {
      return null;
    }
  }

  /**
   * Closes the specified database if it is currently opened and only pinned once.
   * @param ctx database context
   * @param db database to be closed
   * @return {@code true} if opened database was closed
   */
  protected static boolean close(final Context ctx, final String db) {
    final Data data = ctx.data();
    return data != null && db.equals(data.meta.name) && ctx.datas.pins(db) == 1 && close(ctx);
  }

  /**
   * Closes the current database.
   * @param ctx database context
   * @return {@code true} if no database was opened, or if database was closed
   */
  protected static boolean close(final Context ctx) {
    return new Close().run(ctx);
  }
}
