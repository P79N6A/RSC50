package org.basex.query.func.client;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ClientInfo extends ClientFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    checkCreate(qc);
    return Str.get(session(qc, false).info().replace("\r\n?", "\n").trim());
  }
}
