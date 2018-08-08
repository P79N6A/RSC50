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
 * Insert attribute primitive.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Lukas Kircher
 */
public final class InsertAttribute extends NodeCopy {
  /**
   * Constructor.
   * @param pre pre
   * @param data data
   * @param ii input info
   * @param nodes node copy insertion sequence
   */
  public InsertAttribute(final int pre, final Data data, final InputInfo ii,
      final ANodeList nodes) {
    super(UpdateType.INSERTATTR, pre, data, ii, nodes);
  }

  @Override
  public void update(final NamePool pool) {
    if(insseq == null) return;
    add(pool);
  }

  @Override
  public void merge(final Update update) {
    final ANodeList newInsert = ((NodeCopy) update).nodes;
    for(final ANode n : newInsert) nodes.add(n);
  }

  @Override
  public void addAtomics(final AtomicUpdateCache auc) {
    auc.addInsert(pre + 1, pre, insseq);
  }
}
