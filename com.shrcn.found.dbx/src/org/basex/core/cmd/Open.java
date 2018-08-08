package org.basex.core.cmd;

import static org.basex.core.Text.DB_CORRUPT;
import static org.basex.core.Text.DB_NOT_FOUND_X;
import static org.basex.core.Text.DB_OPENED_X;
import static org.basex.core.Text.DB_UPDATED_X;
import static org.basex.core.Text.H_INDEX_FORMAT;
import static org.basex.core.Text.NAME_INVALID_X;
import static org.basex.core.Text.PERM_REQUIRED_X;

import java.io.IOException;

import org.basex.core.BaseXException;
import org.basex.core.Command;
import org.basex.core.Context;
import org.basex.core.Databases;
import org.basex.core.MainOptions;
import org.basex.core.locks.DBLocking;
import org.basex.core.locks.LockResult;
import org.basex.core.users.Perm;
import org.basex.data.Data;
import org.basex.data.DiskData;
import org.basex.data.MetaData;
import org.basex.query.value.seq.DBNodes;
import org.basex.util.Util;

/**
 * Evaluates the 'open' command and opens a database.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Open extends Command {
  /**
   * Default constructor.
   * @param name name of database
   */
  public Open(final String name) {
    this(name, null);
  }

  /**
   * Default constructor.
   * @param name name of database
   * @param path database path (can be {@code null})
   */
  public Open(final String name, final String path) {
    super(Perm.NONE, name, path == null ? "" : path);
  }

  @Override
  protected boolean run() {
    final String db = args[0];
    if(!Databases.validName(db)) return error(NAME_INVALID_X, db);

    // check if database is already opened
    Data data = context.data();
    if(data == null || !data.meta.name.equals(db)) {
      close(context);
      try {
        data = open(db, context, options);
        context.openDB(data);

        final String path = args[1];
        if(!path.isEmpty()) {
          context.current(new DBNodes(data, data.resources.docs(path).toArray()));
        }
        if(data.meta.oldindex()) info(H_INDEX_FORMAT);
        if(data.meta.corrupt)  info(DB_CORRUPT);
      } catch(final IOException ex) {
        return error(Util.message(ex));
      }
    }
    return info(DB_OPENED_X, db, job().performance);
  }

  @Override
  public void databases(final LockResult lr) {
    lr.read.add(DBLocking.CONTEXT).add(args[0]);
  }

  @Override
  public boolean newData(final Context ctx) {
    return close(ctx);
  }

  /**
   * Opens the specified database.
   * @param name name of database
   * @param context database context
   * @param options main options
   * @return data reference
   * @throws IOException I/O exception
   */
  public static Data open(final String name, final Context context, final MainOptions options)
      throws IOException {

    // check permissions
    if(!context.perm(Perm.READ, name)) throw new BaseXException(PERM_REQUIRED_X, Perm.READ);

    synchronized(context.datas) {
      Data data = context.datas.pin(name);
      if(data == null) {
        // check if the addressed database exists
        if(!context.soptions.dbExists(name)) throw new BaseXException(DB_NOT_FOUND_X, name);

        // do not open a database that is currently updated
        final MetaData meta = new MetaData(name, options, context.soptions);
        if(meta.updateFile().exists()) throw new BaseXException(DB_UPDATED_X, meta.name);

        // open database
        data = new DiskData(meta);
        context.datas.pin(data);
      }
      return data;
    }
  }
}
