package org.basex.core.cmd;

import static org.basex.core.Text.BACKUPS_X;
import static org.basex.core.Text.NAME;
import static org.basex.core.Text.SIZE;

import java.io.IOException;

import org.basex.core.Context;
import org.basex.core.locks.DBLocking;
import org.basex.core.locks.LockResult;
import org.basex.core.parse.CmdBuilder;
import org.basex.core.parse.Commands.Cmd;
import org.basex.core.parse.Commands.CmdShow;
import org.basex.io.IO;
import org.basex.io.IOFile;
import org.basex.util.Table;
import org.basex.util.list.TokenList;

/**
 * Evaluates the 'show backups' command and shows available backups.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ShowBackups extends ABackup {
  @Override
  protected boolean run() throws IOException {
    final Table table = new Table();
    table.description = BACKUPS_X;
    table.header.add(NAME);
    table.header.add(SIZE);

    final IOFile dbpath = soptions.dbPath();
    for(final String name : context.databases.backups()) {
      final TokenList tl = new TokenList();
      tl.add(name);
      tl.add(new IOFile(dbpath, name + IO.ZIPSUFFIX).length());
      table.contents.add(tl);
    }
    out.println(table.sort().finish());
    return true;
  }

  @Override
  public boolean updating(final Context ctx) {
    return false;
  }

  @Override
  public void build(final CmdBuilder cb) {
    cb.init(Cmd.SHOW + " " + CmdShow.BACKUPS);
  }

  @Override
  public void databases(final LockResult lr) {
    lr.read.add(DBLocking.BACKUP);
  }
}
