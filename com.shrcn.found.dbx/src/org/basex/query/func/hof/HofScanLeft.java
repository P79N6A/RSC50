package org.basex.query.func.hof;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.FItem;
import org.basex.query.value.item.Item;

/**
 * Implements the {@code hof:scan-left($seq, $start, $f)} function.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public final class HofScanLeft extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final Iter outer = qc.iter(exprs[0]);
    final FItem f = checkArity(exprs[2], 2, qc);
    return new Iter() {
      private Value acc = qc.value(exprs[1]);
      private Iter inner = acc.iter();
      @Override
      public Item next() throws QueryException {
        while(true) {
          final Item i = inner.next();
          if(i != null) return i;
          final Item o = outer.next();
          if(o == null) return null;
          acc = f.invokeValue(qc, info, acc, o);
          inner = acc.iter();
        }
      }
    };
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    return exprs[0].isEmpty() ? exprs[1] : this;
  }
}
