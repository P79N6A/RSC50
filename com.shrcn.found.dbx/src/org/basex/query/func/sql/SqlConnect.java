package org.basex.query.func.sql;

import static org.basex.query.QueryError.BXSQ_ERROR_X;
import static org.basex.util.Token.string;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.Strings;
import org.basex.util.options.Options;

/**
 * Functions on relational databases.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Rositsa Shadura
 */
public final class SqlConnect extends SqlFn {
  /** Auto-commit mode. */
  private static final String AUTOCOMMIT = "autocommit";
  /** User. */
  private static final String USER = "user";
  /** Password. */
  private static final String PASS = "password";

  @SuppressWarnings("resource")
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    checkCreate(qc);
    // URL to relational database
    final String url = string(toToken(exprs[0], qc));
    final JDBCConnections jdbc = jdbc(qc);
    final Connection conn;
    try {
      if(exprs.length > 2) {
        // credentials
        final String user = string(toToken(exprs[1], qc));
        final String pass = string(toToken(exprs[2], qc));
        if(exprs.length == 4) {
          // parse connection options
          final HashMap<String, String> options = toOptions(3, new Options(), qc).free();

          // prepares connection properties
          final Properties props = new Properties();
          for(final Entry<String, String> entry : options.entrySet()) {
            final String key = entry.getKey(), value = entry.getValue();
            if(!key.equals(AUTOCOMMIT)) props.setProperty(key, value);
          }
          props.setProperty(USER, user);
          props.setProperty(PASS, pass);

          // open connection and set auto-commit mode
          conn = DriverManager.getConnection(url, props);
          if(options.containsKey(AUTOCOMMIT)) {
            conn.setAutoCommit(Strings.yes(options.get(AUTOCOMMIT)));
          }
        } else {
          conn = DriverManager.getConnection(url, user, pass);
        }
      } else {
        conn = DriverManager.getConnection(url);
      }
      return Int.get(jdbc.add(conn));
    } catch(final SQLException ex) {
      throw BXSQ_ERROR_X.get(info, ex);
    }
  }
}
