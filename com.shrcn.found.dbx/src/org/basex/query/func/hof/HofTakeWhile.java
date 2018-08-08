package org.basex.query.func.hof;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.FItem;
import org.basex.query.value.item.Item;
import org.basex.query.value.seq.Empty;

/**
 * Implements the {@code hof:take-while($seq, $pred)} function.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public final class HofTakeWhile extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final Iter in = qc.iter(exprs[0]);
    final FItem pred = checkArity(exprs[1], 1, qc);
    return new Iter() {
      @Override
      public Item next() throws QueryException {
        final Item it = in.next();
        if(it != null && toBoolean(pred.invokeValue(qc, info, it), qc)) return it;
        return null;
      }
    };
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final ValueBuilder vb = new ValueBuilder();
    final FItem pred = checkArity(exprs[1], 1, qc);
    final Iter iter = qc.iter(exprs[0]);
    for(Item it; (it = iter.next()) != null;) {
      if(!pred.invokeValue(qc, info, it).ebv(qc, info).bool(info)) break;
      vb.add(it);
    }
    return vb.value();
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    return exprs[0].isEmpty() ? Empty.SEQ : this;
  }
}
