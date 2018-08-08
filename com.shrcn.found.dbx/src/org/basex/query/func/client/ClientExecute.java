package org.basex.query.func.client;

import static org.basex.query.QueryError.BXCL_COMMAND_X;
import static org.basex.query.QueryError.BXCL_COMM_X;

import java.io.IOException;

import org.basex.api.client.ClientSession;
import org.basex.core.BaseXException;
import org.basex.io.out.ArrayOutput;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;
import org.basex.util.Token;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ClientExecute extends ClientFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    checkCreate(qc);
    final ClientSession cs = session(qc, false);
    final String cmd = Token.string(toToken(exprs[1], qc));

    try {
      final ArrayOutput ao = new ArrayOutput();
      cs.setOutputStream(ao);
      cs.execute(cmd);
      cs.setOutputStream(null);
      return Str.get(ao.finish());
    } catch(final BaseXException ex) {
      throw BXCL_COMMAND_X.get(info, ex);
    } catch(final IOException ex) {
      throw BXCL_COMM_X.get(info, ex);
    }
  }
}
