package org.basex.query.iter;

import java.util.Iterator;

import org.basex.query.value.Value;
import org.basex.query.value.node.ANode;
import org.basex.query.value.seq.Empty;
import org.basex.util.Util;

/**
 * Basic node iterator, throwing no exceptions.
 *
 * This class also implements the {@link Iterable} interface, which is why all of its
 * values can also be retrieved via enhanced for(for-each) loops. Note, however, that
 * the {@link #next()} method will give you better performance.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public abstract class BasicNodeIter extends NodeIter implements Iterable<ANode> {
  /** Empty iterator. */
  public static final BasicNodeIter EMPTY = new BasicNodeIter() {
    @Override public ANode next() { return null; }
    @Override public long size() { return 0; }
    @Override public Value value() { return Empty.SEQ; }
  };

  @Override
  public abstract ANode next();

  @Override
  public final Iterator<ANode> iterator() {
    return new Iterator<ANode>() {
      private ANode node;

      @Override
      public boolean hasNext() {
        final ANode n = BasicNodeIter.this.next();
        node = n;
        return n != null;
      }

      @Override
      public ANode next() {
        return node;
      }

      @Override
      public void remove() {
        throw Util.notExpected();
      }
    };
  }
}
