package org.basex.core.cmd;

import static org.basex.core.Text.RES_DELETED_X_X;

import org.basex.core.users.Perm;
import org.basex.data.Data;
import org.basex.io.IOFile;
import org.basex.query.up.atomic.AtomicUpdateCache;
import org.basex.util.list.IntList;
import org.basex.util.list.TokenList;

/**
 * Evaluates the 'delete' command and deletes resources from a database.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Delete extends ACreate {
  /**
   * Default constructor.
   * @param target target to delete
   */
  public Delete(final String target) {
    super(Perm.WRITE, true, target);
  }

  @Override
  protected boolean run() {
    final Data data = context.data();
    final String target = args[0];

    if(!startUpdate(data)) return false;

    // delete all documents
    final IntList docs = data.resources.docs(target);
    final AtomicUpdateCache auc = new AtomicUpdateCache(data);
    final int ds = docs.size();
    for(int d = 0; d < ds; d++) auc.addDelete(docs.get(d));
    auc.execute(false);
    context.invalidate();

    // delete binaries
    final TokenList bins = data.resources.binaries(target);
    delete(data, target);

    // finish update
    if(!finishUpdate(data)) return false;

    // return info message
    return info(RES_DELETED_X_X, docs.size() + bins.size(), job().performance);
  }

  /**
   * Deletes the specified resources.
   * @param data data reference
   * @param res resource to be deleted
   */
  public static void delete(final Data data, final String res) {
    if(data.inMemory()) return;
    final IOFile file = data.meta.binary(res);
    if(file != null) file.delete();
  }
}
