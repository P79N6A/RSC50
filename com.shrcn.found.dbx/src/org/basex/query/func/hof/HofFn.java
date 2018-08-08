package org.basex.query.func.hof;

import java.util.Comparator;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryRTException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.FItem;
import org.basex.query.value.item.Item;
import org.basex.query.value.seq.Empty;

/**
 * Higher-order function.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
abstract class HofFn extends StandardFunc {
  /**
   * Gets a comparator from a less-than predicate as function item.
   * The {@link Comparator#compare(Object, Object)} method throws a
   * {@link QueryRTException} if the comparison throws a {@link QueryException}.
   * @param pos argument position of the predicate
   * @param qc query context
   * @return comparator
   * @throws QueryException exception
   */
  Comparator<Item> getComp(final int pos, final QueryContext qc) throws QueryException {
    final FItem lt = checkArity(exprs[pos], 2, qc);
    return new Comparator<Item>() {
      @Override
      public int compare(final Item a, final Item b) {
        try {
          return toBoolean(lt.invokeItem(qc, info, a == null ? Empty.SEQ : a,
              b == null ? Empty.SEQ : b)) ? -1 : 1;
        } catch(final QueryException qe) {
          throw new QueryRTException(qe);
        }
      }
    };
  }
}
