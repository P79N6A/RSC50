package org.basex.api.client;

import java.io.InputStream;
import java.io.OutputStream;

import org.basex.core.BaseXException;
import org.basex.core.Command;
import org.basex.core.Context;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.Close;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.Replace;
import org.basex.core.cmd.Store;
import org.basex.core.parse.CommandParser;
import org.basex.core.users.User;
import org.basex.query.QueryException;
import org.basex.server.LoginException;

/**
 * This class offers methods to locally execute database commands.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class LocalSession extends Session {
  /** Database context. */
  private final Context ctx;

  /**
   * Default constructor.
   * @param context context
   */
  public LocalSession(final Context context) {
    this(context, null);
  }

  /**
   * Constructor, specifying an output stream.
   * @param context context
   * @param output client output; if set to {@code null}, results will be returned as strings
   */
  public LocalSession(final Context context, final OutputStream output) {
    this(context, output, context.user());
  }

  /**
   * Constructor, specifying login data.
   * @param context context
   * @param username user name
   * @param password password (plain text)
   * @throws LoginException login exception
   */
  public LocalSession(final Context context, final String username, final String password)
      throws LoginException {
    this(context, username, password, null);
  }

  /**
   * Constructor, specifying login data and an output stream.
   * @param context context
   * @param username user name
   * @param password password (plain text)
   * @param output client output; if set to {@code null}, results will be returned as strings
   * @throws LoginException login exception
   */
  public LocalSession(final Context context, final String username, final String password,
      final OutputStream output) throws LoginException {

    this(context, output, context.users.get(username));
    final User user = ctx.user();
    if(user == null || !user.matches(password)) throw new LoginException();
  }

  /**
   * Constructor, specifying an output stream.
   * @param context context
   * @param output output stream
   * @param user user
   */
  private LocalSession(final Context context, final OutputStream output, final User user) {
    super(output);
    ctx = new Context(context, null);
    ctx.user(user);
  }

  @Override
  public void create(final String name, final InputStream input) throws BaseXException {
    execute(new CreateDB(name), input);
  }

  @Override
  public void add(final String path, final InputStream input) throws BaseXException {
    execute(new Add(path), input);
  }

  @Override
  public void replace(final String path, final InputStream input) throws BaseXException {
    execute(new Replace(path), input);
  }

  @Override
  public void store(final String path, final InputStream input) throws BaseXException {
    execute(new Store(path), input);
  }

  /**
   * Executes a command, passing the specified input.
   * @param cmd command
   * @param input input stream
   * @throws BaseXException database exception
   */
  private void execute(final Command cmd, final InputStream input) throws BaseXException {
    cmd.setInput(input);
    cmd.execute(ctx);
    info = cmd.info();
  }

  @Override
  public LocalQuery query(final String query) {
    return new LocalQuery(query, ctx, out);
  }

  @Override
  public synchronized void close() {
    new Close().run(ctx);
  }

  @Override
  protected void execute(final String command, final OutputStream output) throws BaseXException {
    try {
      execute(new CommandParser(command, ctx).parseSingle(), output);
    } catch(final QueryException ex) {
      throw new BaseXException(ex);
    }
  }

  @Override
  protected void execute(final Command command, final OutputStream output) throws BaseXException {
    command.execute(ctx, output);
    info = command.info();
  }

  /**
   * Returns the associated database context.
   * Called from the XQJ driver.
   * @return database context
   */
  public Context context() {
    return ctx;
  }
}
