package org.basex.query.func.db;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.DBNode;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbNodeId extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return new Iter() {
      final Iter ir = qc.iter(exprs[0]);

      @Override
      public Int next() throws QueryException {
        final Item it = ir.next();
        if(it == null) return null;
        final DBNode node = toDBNode(it);
        return Int.get(node.data().id(node.pre()));
      }
    };
  }
}
