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
 * Insert into as last primitive.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Lukas Kircher
 */
public final class InsertIntoAsLast extends NodeCopy {
  /**
   * Constructor for an insertInto which is part of a replaceElementContent substitution.
   * @param pre target pre value
   * @param data target data instance
   * @param ii input info
   * @param nodes node copy insertion sequence
   */
  public InsertIntoAsLast(final int pre, final Data data, final InputInfo ii,
      final ANodeList nodes) {
    super(UpdateType.INSERTINTOLAST, pre, data, ii, nodes);
  }

  @Override
  public void merge(final Update update) {
    final ANodeList newInsert = ((NodeCopy) update).nodes;
    for(final ANode n : newInsert) nodes.add(n);
  }

  @Override
  public void addAtomics(final AtomicUpdateCache auc) {
    final int s = data.size(pre, data.kind(pre));
    auc.addInsert(pre + s, pre, insseq);
  }

  @Override
  public void update(final NamePool pool) { }
}
