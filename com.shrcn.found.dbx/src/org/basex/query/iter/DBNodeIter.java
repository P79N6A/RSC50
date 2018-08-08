package org.basex.query.iter;

import org.basex.data.Data;
import org.basex.query.QueryException;
import org.basex.query.value.Value;
import org.basex.query.value.node.DBNode;
import org.basex.query.value.seq.DBNodeSeq;
import org.basex.util.list.IntList;

/**
 * Database node iterator.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public abstract class DBNodeIter extends BasicNodeIter {
  /** Data reference. */
  protected final Data data;

  /**
   * Constructor.
   * @param data data reference
   */
  public DBNodeIter(final Data data) {
    this.data = data;
  }

  @Override
  public abstract DBNode next();

  @Override
  public Value value() throws QueryException {
    final IntList il = new IntList();
    for(DBNode n; (n = next()) != null;) il.add(n.pre());
    return DBNodeSeq.get(il, data, false, false);
  }
}
