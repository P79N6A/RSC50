package org.basex.core.cmd;

import static org.basex.core.Text.NO_MAINMEM;
import static org.basex.core.Text.PATH_INVALID_X;
import static org.basex.core.Text.QUERY_EXECUTED_X_X;
import static org.basex.core.Text.RES_NOT_FOUND_X;

import java.io.IOException;

import org.basex.core.locks.DBLocking;
import org.basex.core.locks.LockResult;
import org.basex.core.users.Perm;
import org.basex.data.Data;
import org.basex.data.MetaData;
import org.basex.io.IOFile;
import org.basex.io.in.BufferInput;

/**
 * Evaluates the 'retrieve' command and retrieves binary content.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Retrieve extends ACreate {
  /**
   * Default constructor.
   * @param path source path
   */
  public Retrieve(final String path) {
    super(Perm.NONE, true, path);
  }

  @Override
  protected boolean run() {
    final String path = MetaData.normPath(args[0]);
    if(path == null) return error(PATH_INVALID_X, args[0]);

    final Data data = context.data();
    if(data.inMemory()) return error(NO_MAINMEM);

    final IOFile bin = data.meta.binary(path);
    if(bin == null || !bin.exists() || bin.isDir()) return error(RES_NOT_FOUND_X, path);

    try(final BufferInput bi = new BufferInput(bin)) {
      for(int b; (b = bi.read()) != -1;) out.write(b);
      return info(QUERY_EXECUTED_X_X, "", job().performance);
    } catch(final IOException ex) {
      return error(ex.toString());
    }
  }

  @Override
  public void databases(final LockResult lr) {
    lr.read.add(DBLocking.CONTEXT);
  }
}
