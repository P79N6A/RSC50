package org.basex.query.func.db;

import org.basex.core.cmd.InfoDB;
import org.basex.data.Data;
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
public final class DbInfo extends DbAccess {
  /** Resource element name. */
  private static final String DATABASE = "database";

  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Data data = checkData(qc);
    return toNode(InfoDB.db(data.meta, false, true), DATABASE);
  }
}
