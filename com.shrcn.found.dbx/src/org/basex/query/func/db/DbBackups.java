package org.basex.query.func.db;

import static org.basex.util.Token.string;
import static org.basex.util.Token.token;

import java.util.Date;

import org.basex.core.Databases;
import org.basex.io.IO;
import org.basex.io.IOFile;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Dtm;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.FElem;
import org.basex.util.DateTime;
import org.basex.util.list.StringList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbBackups extends StandardFunc {
  /** Backup string. */
  private static final String BACKUP = "backup";
  /** Size string. */
  private static final String SIZE = "size";
  /** Date string. */
  private static final String DATE = "date";
  /** Database string. */
  private static final String DATABASE = "database";

  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    checkCreate(qc);
    final String name = exprs.length == 0 ? null : string(toToken(exprs[0], qc));

    final StringList backups = name == null ? qc.context.databases.backups() :
      qc.context.databases.backups(name);
    final IOFile dbpath = qc.context.soptions.dbPath();
    return new Iter() {
      int up = -1;

      @Override
      public Item next() throws QueryException {
        if(++up >= backups.size()) return null;
        final String backup = backups.get(up);
        final long length = new IOFile(dbpath, backup + IO.ZIPSUFFIX).length();
        final String db = Databases.name(backup);

        final Date date = Databases.date(backup);
        final String ymd = DateTime.format(date, DateTime.DATE);
        final String hms = DateTime.format(date, DateTime.TIME);
        final Dtm dtm = new Dtm(token(ymd + 'T' + hms), info);

        return new FElem(BACKUP).add(backup).add(DATABASE, db).add(DATE, dtm.string(info)).
            add(SIZE, token(length));
      }
    };
  }
}
