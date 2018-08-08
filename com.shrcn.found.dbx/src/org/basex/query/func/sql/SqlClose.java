package org.basex.query.func.sql;

import static org.basex.query.QueryError.BXSQ_ERROR_X;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Functions on relational databases.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Rositsa Shadura
 */
public final class SqlClose extends SqlFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    checkCreate(qc);

    final int id = (int) toLong(exprs[0], qc);
    final JDBCConnections jdbc = jdbc(qc);
    try(final AutoCloseable sql = jdbc.get(id)) {
      // try-with-resources: resource will automatically be closed
      jdbc.remove(id);
    } catch(final Exception ex) {
      throw BXSQ_ERROR_X.get(info, ex);
    }
    return null;
  }
}
