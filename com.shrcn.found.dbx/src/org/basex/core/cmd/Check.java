package org.basex.core.cmd;

import java.io.IOException;

import org.basex.core.Command;
import org.basex.core.Context;
import org.basex.core.locks.DBLocking;
import org.basex.core.locks.LockResult;
import org.basex.core.users.Perm;
import org.basex.data.MetaData;
import org.basex.io.IO;

/**
 * Evaluates the 'check' command: opens an existing database or
 * creates a new one.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Check extends Command {
  /**
   * Default constructor.
   * @param input input (name of database, file path, XML string)
   */
  public Check(final String input) {
    super(Perm.NONE, input);
  }

  @Override
  protected boolean run() {
    // close existing database
    close(context);

    // input (can be path or XML string)
    final String input = args[0];
    // get path and database name, overwrite database name with generated name
    final IO io = IO.get(input);
    final String dbName = io.dbName();

    // choose OPEN if user has no create permissions, or if database exists
    final Command cmd;
    if(open(io, dbName)) {
      cmd = new Open(dbName);
    } else {
      cmd = new CreateDB(dbName, io.exists() ? input : null);
    }

    // execute command
    try {
      final boolean ok = pushJob(cmd).run(context);
      final String msg = cmd.info().trim();
      return ok ? info(msg) : error(msg);
    } finally {
      popJob();
    }
  }

  /**
   * Checks if the addressed database can be opened, or needs to be (re)built.
   * @param input query input
   * @param dbName database name
   * @return result of check
   */
  private boolean open(final IO input, final String dbName) {
    // minimum permissions: create
    if(!context.user().has(Perm.CREATE)) return true;
    // database with given name does not exist
    if(!soptions.dbPath(dbName).exists()) return false;
    // open database if addressed file does not exist
    if(!input.exists()) return true;

    // compare timestamp of database input and specified file; rebuild invalid databases
    final MetaData meta = new MetaData(dbName, options, soptions);
    try {
      meta.read();
    } catch(final IOException ex) {
      return false;
    }
    return meta.time == input.timeStamp();
  }

  @Override
  public boolean supportsProg() {
    return true;
  }

  @Override
  public boolean stoppable() {
    return true;
  }

  @Override
  public void databases(final LockResult lr) {
    lr.read.add(DBLocking.CONTEXT).add(IO.get(args[0]).dbName());
  }

  @Override
  public boolean newData(final Context ctx) {
    return close(ctx);
  }
}
