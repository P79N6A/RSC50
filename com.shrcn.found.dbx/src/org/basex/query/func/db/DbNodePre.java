package org.basex.query.func.db;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbNodePre extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return new Iter() {
      final Iter ir = qc.iter(exprs[0]);

      @Override
      public Int next() throws QueryException {
        final Item it = ir.next();
        return it == null ? null : Int.get(toDBNode(it).pre());
      }
    };
  }
}
