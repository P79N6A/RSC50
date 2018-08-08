package org.basex.query.up.primitives.node;

import static org.basex.query.QueryError.UPMULTREN_X;

import org.basex.data.Data;
import org.basex.data.MemData;
import org.basex.query.QueryException;
import org.basex.query.up.NamePool;
import org.basex.query.up.atomic.AtomicUpdateCache;
import org.basex.query.up.primitives.Update;
import org.basex.query.up.primitives.UpdateType;
import org.basex.query.value.item.QNm;
import org.basex.query.value.node.DBNode;
import org.basex.util.InputInfo;
import org.basex.util.Util;

/**
 * Rename node primitive.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Lukas Kircher
 */
public final class RenameNode extends NodeUpdate {
  /** New name. */
  private final QNm name;

  /**
   * Constructor.
   * @param pre target node pre value
   * @param data target data reference
   * @param ii input info
   * @param name new QName / new name value
   */
  public RenameNode(final int pre, final Data data, final InputInfo ii, final QNm name) {
    super(UpdateType.RENAMENODE, pre, data, ii);
    this.name = name;
  }

  @Override
  public void prepare(final MemData tmp) { }

  @Override
  public void merge(final Update update) throws QueryException {
    throw UPMULTREN_X.get(info, node());
  }

  @Override
  public void update(final NamePool pool) {
    final DBNode node = node();
    pool.add(name, node.nodeType());
    pool.remove(node);
  }

  @Override
  public String toString() {
    return Util.className(this) + '[' + node() + ", " + name + ']';
  }

  @Override
  public int size() {
    return 1;
  }

  @Override
  public void addAtomics(final AtomicUpdateCache auc) {
    auc.addRename(pre, name.string(), name.uri());
  }
}
