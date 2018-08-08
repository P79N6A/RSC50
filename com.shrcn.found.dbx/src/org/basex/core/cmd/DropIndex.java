package org.basex.core.cmd;

import static org.basex.core.Text.INDEX_DROPPED_X_X;
import static org.basex.core.Text.UNKNOWN_CMD_X;

import java.io.IOException;

import org.basex.core.parse.CmdBuilder;
import org.basex.core.parse.Commands.Cmd;
import org.basex.core.parse.Commands.CmdDrop;
import org.basex.core.parse.Commands.CmdIndex;
import org.basex.core.users.Perm;
import org.basex.data.Data;
import org.basex.index.IndexType;
import org.basex.util.Util;

/**
 * Evaluates the 'drop index' command and deletes indexes in the currently
 * opened database.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DropIndex extends ACreate {
  /**
   * Constructor.
   * @param type index type, defined in {@link CmdIndex}
   */
  public DropIndex(final Object type) {
    super(Perm.WRITE, true, type.toString());
  }

  @Override
  protected boolean run() {
    final Data data = context.data();
    final CmdIndex ci = getOption(CmdIndex.class);
    final IndexType type;
    if(ci == CmdIndex.TEXT) {
      type = IndexType.TEXT;
      data.meta.createtext = false;
    } else if(ci == CmdIndex.ATTRIBUTE) {
      type = IndexType.ATTRIBUTE;
      data.meta.createattr = false;
    } else if(ci == CmdIndex.TOKEN) {
      type = IndexType.TOKEN;
      data.meta.createtoken = false;
    } else if(ci == CmdIndex.FULLTEXT) {
      type = IndexType.FULLTEXT;
      data.meta.createft = false;
    } else {
      return error(UNKNOWN_CMD_X, this);
    }
    data.meta.names(type, options);

    if(!startUpdate(data)) return false;
    boolean ok = true;
    try {
      drop(type, data);
      ok = info(INDEX_DROPPED_X_X, type, job().performance);
    } catch(final IOException ex) {
      ok = error(Util.message(ex));
    } finally {
      ok &= finishUpdate(data);
    }
    return ok;
  }

  @Override
  public void build(final CmdBuilder cb) {
    cb.init(Cmd.DROP + " " + CmdDrop.INDEX).args();
  }

  /**
   * Drops the specified index.
   * @param type index type
   * @param data data reference
   * @throws IOException I/O exception
   */
  static void drop(final IndexType type, final Data data) throws IOException {
    data.meta.dirty = true;
    data.meta.index(type, false);
    data.dropIndex(type);
  }
}
