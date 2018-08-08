package org.basex.query.func.client;

import static org.basex.query.QueryError.BXCL_CONN_X;

import java.io.IOException;

import org.basex.api.client.ClientSession;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.Token;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ClientConnect extends ClientFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    checkCreate(qc);

    final String host = Token.string(toToken(exprs[0], qc));
    final String user = Token.string(toToken(exprs[2], qc));
    final String pass = Token.string(toToken(exprs[3], qc));
    final int port = (int) toLong(exprs[1], qc);
    try {
      return sessions(qc).add(new ClientSession(host, port, user, pass));
    } catch(final IOException ex) {
      throw BXCL_CONN_X.get(info, ex);
    }
  }
}
