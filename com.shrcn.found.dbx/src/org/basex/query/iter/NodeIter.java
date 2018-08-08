package org.basex.query.iter;

import org.basex.query.QueryException;
import org.basex.query.value.node.ANode;

/**
 * ANode iterator interface.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public abstract class NodeIter extends Iter {
  @Override
  public abstract ANode next() throws QueryException;

  @Override
  public ANode get(final long i) {
    return null;
  }
}
