package org.basex.query.up.primitives.node;

import org.basex.data.Data;
import org.basex.query.up.NamePool;
import org.basex.query.up.atomic.AtomicUpdateCache;
import org.basex.query.up.primitives.Update;
import org.basex.query.up.primitives.UpdateType;
import org.basex.query.util.list.ANodeList;
import org.basex.query.value.node.ANode;
import org.basex.util.InputInfo;

/**
 * Insert after primitive.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Lukas Kircher
 */
public final class InsertAfter extends NodeCopy {
  /**
   * Constructor.
   * @param pre target pre value
   * @param data target data instance
   * @param ii input info
   * @param nodes node copy insertion sequence
   */
  public InsertAfter(final int pre, final Data data, final InputInfo ii, final ANodeList nodes) {
    super(UpdateType.INSERTAFTER, pre, data, ii, nodes);
  }

  @Override
  public void merge(final Update update) {
    final ANodeList newInsert = ((NodeCopy) update).nodes;
    for(final ANode n : newInsert) nodes.add(n);
  }

  @Override
  public void addAtomics(final AtomicUpdateCache auc) {
    final int k = data.kind(pre);
    final int s = data.size(pre, k);
    auc.addInsert(pre + s, data.parent(pre, k), insseq);
  }

  @Override
  public void update(final NamePool pool) { }
}
