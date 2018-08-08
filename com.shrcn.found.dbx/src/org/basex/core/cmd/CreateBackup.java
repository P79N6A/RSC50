package org.basex.core.cmd;

import static org.basex.core.Text.BACKUP;
import static org.basex.core.Text.DB_BACKUP_X;
import static org.basex.core.Text.DB_NOT_BACKUP_X;
import static org.basex.core.Text.DB_NOT_FOUND_X;
import static org.basex.core.Text.DB_UPDATED_X;
import static org.basex.core.Text.NAME_INVALID_X;
import static org.basex.data.DataText.DATAUPD;

import java.io.IOException;
import java.util.Date;

import org.basex.core.Databases;
import org.basex.core.StaticOptions;
import org.basex.core.locks.LockResult;
import org.basex.core.parse.CmdBuilder;
import org.basex.core.parse.Commands.Cmd;
import org.basex.core.parse.Commands.CmdCreate;
import org.basex.core.users.Perm;
import org.basex.data.MetaData;
import org.basex.io.IO;
import org.basex.io.IOFile;
import org.basex.io.Zip;
import org.basex.util.DateTime;
import org.basex.util.list.StringList;

/**
 * Evaluates the 'backup' command and creates a backup of a database.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class CreateBackup extends ABackup {
  /**
   * Default constructor.
   * @param arg optional argument
   */
  public CreateBackup(final String arg) {
    super(arg);
  }

  @Override
  protected boolean run() {
    final String name = args[0];
    if(!Databases.validName(name, true)) return error(NAME_INVALID_X, name);

    // retrieve all databases
    final StringList dbs = context.filter(Perm.READ, context.databases.listDBs(name));
    if(dbs.isEmpty()) return error(DB_NOT_FOUND_X, name);

    // loop through all databases
    boolean ok = true;
    for(final String db : dbs) {
      // don't open databases marked as updating
      if(MetaData.file(soptions.dbPath(db), DATAUPD).exists()) {
        // reject backups of databases that are currently being updated (or corrupt)
        info(DB_UPDATED_X, db);
        ok = false;
      } else {
        try {
          backup(db, soptions, this);
          // backup was successful
          info(DB_BACKUP_X, db, job().performance);
        } catch(final IOException ex) {
          info(DB_NOT_BACKUP_X, db);
          ok = false;
        }
      }
    }
    return ok;
  }

  /**
   * Backups the specified database.
   * @param db name of the database
   * @param sopts static options
   * @param cmd calling command instance
   * @throws IOException I/O Exception
   */
  public static void backup(final String db, final StaticOptions sopts, final CreateBackup cmd)
      throws IOException {

    final String backup = db + '-' + DateTime.format(new Date(), DateTime.DATETIME) + IO.ZIPSUFFIX;
    final IOFile zf = sopts.dbPath(backup);
    final Zip zip = new Zip(zf);

    try {
      if(cmd != null) cmd.pushJob(zip);
      final IOFile dbpath = sopts.dbPath(db);
      final StringList files = dbpath.descendants();
      // delete file indicating an update (this file is generated when using XQuery)
      files.delete(DATAUPD + IO.BASEXSUFFIX);
      zip.zip(dbpath, files);
    } finally {
      if(cmd != null) cmd.popJob();
    }
  }

  @Override
  public void databases(final LockResult lr) {
    super.databases(lr);
    databases(lr.read, 0);
  }

  @Override
  public String shortInfo() {
    return BACKUP;
  }

  @Override
  public boolean supportsProg() {
    return true;
  }

  @Override
  public void build(final CmdBuilder cb) {
    cb.init(Cmd.CREATE + " " + CmdCreate.BACKUP).args();
  }
}
