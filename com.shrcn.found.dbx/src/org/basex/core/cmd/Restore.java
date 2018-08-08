package org.basex.core.cmd;

import static org.basex.core.Text.BACKUP_NOT_FOUND_X;
import static org.basex.core.Text.DB_NOT_RESTORED_X;
import static org.basex.core.Text.DB_PINNED_X;
import static org.basex.core.Text.DB_RESTORED_X;
import static org.basex.core.Text.NAME_INVALID_X;
import static org.basex.core.Text.RESTORE;

import java.io.IOException;

import org.basex.core.Context;
import org.basex.core.Databases;
import org.basex.core.StaticOptions;
import org.basex.core.locks.LockResult;
import org.basex.io.IO;
import org.basex.io.IOFile;
import org.basex.io.Zip;
import org.basex.util.Util;
import org.basex.util.list.StringList;

/**
 * Evaluates the 'restore' command and restores a backup of a database.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Restore extends ABackup {
  /** States if current database was closed. */
  private boolean closed;

  /**
   * Default constructor.
   * @param arg optional argument
   */
  public Restore(final String arg) {
    super(arg);
  }

  @Override
  protected boolean run() {
    final String name = args[0];
    if(!Databases.validName(name)) return error(NAME_INVALID_X, name);

    // find backup with or without date suffix
    final StringList backups = context.databases.backups(name);
    if(backups.isEmpty()) return error(BACKUP_NOT_FOUND_X, name);

    final String backup = backups.get(0);
    final String db = Databases.name(backup);

    // close database if it's currently opened and not opened by others
    if(!closed) closed = close(context, db);
    // check if database is still pinned
    if(context.pinned(db)) return error(DB_PINNED_X, db);

    // try to restore database
    try {
      restore(db, backup, soptions, this);
      return !closed || new Open(db).run(context) ? info(DB_RESTORED_X, backup, job().performance) :
        error(DB_NOT_RESTORED_X, db);
    } catch(final IOException ex) {
      Util.debug(ex);
      return error(DB_NOT_RESTORED_X, db);
    }
  }

  @Override
  public void databases(final LockResult lr) {
    super.databases(lr);
    // Not sure whether database or name of backup file is provided: lock both
    final String backup = args[0];
    lr.write.add(backup).add(Databases.name(backup));
  }

  /**
   * Restores the specified database.
   * @param db name of database
   * @param backup name of backup
   * @param sopts static options
   * @param cmd calling command instance
   * @throws IOException I/O exception
   */
  public static void restore(final String db, final String backup, final StaticOptions sopts,
      final Restore cmd) throws IOException {

    // drop target database
    DropDB.drop(db, sopts);

    final IOFile dbpath = sopts.dbPath();
    final Zip zip = new Zip(new IOFile(dbpath, backup + IO.ZIPSUFFIX));
    try {
      if(cmd != null) cmd.pushJob(zip);
      zip.unzip(dbpath);
    } finally {
      if(cmd != null) cmd.popJob();
    }
  }

  @Override
  public String shortInfo() {
    return RESTORE;
  }

  @Override
  public boolean newData(final Context ctx) {
    closed = close(ctx, args[0]);
    return closed;
  }

  @Override
  public boolean supportsProg() {
    return true;
  }
}
