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
 * Insert into as first primitive.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Lukas Kircher
 */
public final class InsertIntoAsFirst extends NodeCopy {
  /**
   * Constructor.
   * @param pre target node pre value
   * @param data target data reference
   * @param ii input info
   * @param nodes insertion sequence node list
   */
  public InsertIntoAsFirst(final int pre, final Data data, final InputInfo ii,
      final ANodeList nodes) {
    super(UpdateType.INSERTINTOFIRST, pre, data, ii, nodes);
  }

  @Override
  public void merge(final Update update) {
    final ANodeList newInsert = ((NodeCopy) update).nodes;
    for(final ANode n : newInsert) nodes.add(n);
  }

  @Override
  public void addAtomics(final AtomicUpdateCache auc) {
    auc.addInsert(pre + data.attSize(pre, data.kind(pre)), pre, insseq);
  }

  @Override
  public void update(final NamePool pool) { }
}
