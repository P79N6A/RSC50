package org.basex.query;

import org.basex.core.Databases;
import org.basex.io.IO;

/**
 * This class references input passed on in a query. It can be a file path or a database name.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class QueryInput {
  /** Input reference. */
  public final IO io;
  /** Database name ({@code null} indicates that no name can be extracted from original path). */
  public String dbName;
  /** Original input string (resource URI, database path or XML string). */
  final String original;
  /** Database path (empty string indicates root). */
  String dbPath = "";


  /**
   * Constructor.
   * @param original original input string
   * @param sc static context
   */
  public QueryInput(final String original, final StaticContext sc) {
    this.original = original;
    io = sc.resolve(original);

    // check if the specified input string can be rewritten to a database name and path
    String name = original.startsWith("/") ? original.substring(1) : original, path = "";
    final int s = name.indexOf('/');
    if(s != -1) {
      path = name.substring(s + 1);
      name = name.substring(0, s);
    }
    if(Databases.validName(name)) {
      dbName = name;
      dbPath = path;
    }
  }

  @Override
  public String toString() {
    return original;
  }
}
