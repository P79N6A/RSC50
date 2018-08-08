package org.basex.query.func.fn;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.FItem;
import org.basex.query.value.item.Item;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnFilter extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final FItem fun = checkArity(exprs[1], 1, qc);
    final Iter ir = exprs[0].iter(qc);
    return new Iter() {
      @Override
      public Item next() throws QueryException {
        for(Item it; (it = ir.next()) != null;) {
          if(toBoolean(fun.invokeItem(qc, info, it))) return it;
        }
        return null;
      }
    };
  }
}
