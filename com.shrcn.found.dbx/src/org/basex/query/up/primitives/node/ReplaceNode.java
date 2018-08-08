package org.basex.query.up.primitives.node;

import static org.basex.query.QueryError.UPMULTREPL_X;

import org.basex.data.Data;
import org.basex.query.QueryException;
import org.basex.query.up.NamePool;
import org.basex.query.up.atomic.AtomicUpdateCache;
import org.basex.query.up.primitives.Update;
import org.basex.query.up.primitives.UpdateType;
import org.basex.query.util.list.ANodeList;
import org.basex.util.InputInfo;

/**
 * Replace node primitive.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Lukas Kircher
 */
public final class ReplaceNode extends NodeCopy {
  /**
   * Constructor.
   * @param pre target node pre value
   * @param data target data instance
   * @param ii input info
   * @param nodes node copy insertion sequence
   */
  public ReplaceNode(final int pre, final Data data, final InputInfo ii, final ANodeList nodes) {
    super(UpdateType.REPLACENODE, pre, data, ii, nodes);
  }

  @Override
  public void update(final NamePool pool) {
    if(insseq == null) return;
    add(pool);
    pool.remove(node());
  }

  @Override
  public void merge(final Update update) throws QueryException {
    throw UPMULTREPL_X.get(info, node());
  }

  @Override
  public void addAtomics(final AtomicUpdateCache auc) {
    auc.addReplace(pre, insseq);
  }
}
