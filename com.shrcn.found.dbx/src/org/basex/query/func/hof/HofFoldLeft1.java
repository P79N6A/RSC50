package org.basex.query.func.hof;

import static org.basex.query.QueryError.EMPTYFOUND;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryText;
import org.basex.query.expr.Expr;
import org.basex.query.func.DynFuncCall;
import org.basex.query.func.StandardFunc;
import org.basex.query.func.fn.FnForEach;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.FItem;
import org.basex.query.value.item.Item;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public final class HofFoldLeft1 extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final FItem f = checkArity(exprs[1], 2, qc);
    final Iter iter = exprs[0].iter(qc);

    Value sum = checkNoEmpty(iter.next());
    for(Item it; (it = iter.next()) != null;) sum = f.invokeValue(qc, info, sum, it);
    return sum;
  }

  @Override
  protected Expr opt(final CompileContext cc) throws QueryException {
    if(allAreValues() && exprs[0].size() <= FnForEach.UNROLL_LIMIT) {
      cc.info(QueryText.OPTUNROLL_X, this);
      final Value seq = (Value) exprs[0];
      if(seq.isEmpty()) throw EMPTYFOUND.get(info);
      final FItem f = checkArity(exprs[1], 2, cc.qc);
      Expr e = seq.itemAt(0);
      final long is = seq.size();
      for(int i = 1; i < is; i++)
        e = new DynFuncCall(info, sc, f, e, seq.itemAt(i)).optimize(cc);
      return e;
    }
    return this;
  }
}
