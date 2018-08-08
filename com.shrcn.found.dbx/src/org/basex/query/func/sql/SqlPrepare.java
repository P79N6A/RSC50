package org.basex.query.func.sql;

import static org.basex.query.QueryError.BXSQ_ERROR_X;
import static org.basex.util.Token.string;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Functions on relational databases.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Rositsa Shadura
 */
public final class SqlPrepare extends SqlFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    checkCreate(qc);
    final Connection conn = connection(qc);
    // Prepared statement
    final byte[] prepStmt = toToken(exprs[1], qc);
    try {
      // Keep prepared statement
      final PreparedStatement prep = conn.prepareStatement(string(prepStmt));
      return Int.get(jdbc(qc).add(prep));
    } catch(final SQLException ex) {
      throw BXSQ_ERROR_X.get(info, ex);
    }
  }
}
