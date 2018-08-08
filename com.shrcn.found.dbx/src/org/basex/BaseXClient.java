package org.basex;

import static org.basex.core.Text.COLS;
import static org.basex.core.Text.CONNECTION_ERROR_X;
import static org.basex.core.Text.PASSWORD;
import static org.basex.core.Text.USERNAME;

import java.io.IOException;
import java.net.ConnectException;

import org.basex.api.client.ClientSession;
import org.basex.api.client.Session;
import org.basex.core.BaseXException;
import org.basex.core.StaticOptions;
import org.basex.util.Util;

/**
 * This is the starter class for the client console mode.
 * All input is sent to the server instance.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class BaseXClient extends BaseX {
  /**
   * Main method of the database client, launching a client instance.
   * Command-line arguments are listed with the {@code -h} argument.
   * @param args command-line arguments
   */
  public static void main(final String... args) {
    try {
      new BaseXClient(args);
    } catch(final IOException ex) {
      Util.errln(ex);
      System.exit(1);
    }
  }

  /**
   * Constructor.
   * @param args command-line arguments
   * @throws IOException I/O exception
   */
  public BaseXClient(final String... args) throws IOException {
    super(args);
  }

  @Override
  protected boolean local() {
    return false;
  }

  @Override
  protected Session init() throws IOException {
    // user/password input
    String user = context.soptions.get(StaticOptions.USER);
    String pass = context.soptions.get(StaticOptions.PASSWORD);
    while(user.isEmpty()) {
      Util.out(USERNAME + COLS);
      user = Util.input();
    }
    while(pass.isEmpty()) {
      Util.out(PASSWORD + COLS);
      pass = Util.password();
    }

    final String host = context.soptions.get(StaticOptions.HOST);
    final int port = context.soptions.get(StaticOptions.PORT);
    try {
      return new ClientSession(host, port, user, pass, out);
    } catch(final ConnectException ex) {
      throw new BaseXException(CONNECTION_ERROR_X, port);
    }
  }
}
