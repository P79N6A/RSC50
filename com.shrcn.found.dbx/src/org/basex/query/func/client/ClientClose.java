package org.basex.query.func.client;

import static org.basex.query.QueryError.BXCL_COMMAND_X;

import java.io.IOException;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ClientClose extends ClientFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    checkCreate(qc);
    try {
      session(qc, true).close();
      return null;
    } catch(final IOException ex) {
      throw BXCL_COMMAND_X.get(info, ex);
    }
  }
}
