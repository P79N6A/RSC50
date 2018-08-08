package org.basex.query.func.fn;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryText;
import org.basex.query.expr.Expr;
import org.basex.query.func.DynFuncCall;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.FItem;
import org.basex.query.value.item.Item;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnFoldLeft extends StandardFunc {
  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final Iter ir = exprs[0].iter(qc);
    final FItem fun = checkArity(exprs[2], 2, qc);
    Value res = qc.value(exprs[1]);
    for(Item it; (it = ir.next()) != null;) res = fun.invokeValue(qc, info, res, it);
    return res;
  }

  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final Iter ir = exprs[0].iter(qc);
    final FItem fun = checkArity(exprs[2], 2, qc);

    // don't convert to a value if not necessary
    Item it = ir.next();
    if(it == null) return exprs[1].iter(qc);

    Value res = qc.value(exprs[1]);
    do res = fun.invokeValue(qc, info, res, it);
    while((it = ir.next()) != null);
    return res.iter();
  }

  @Override
  protected Expr opt(final CompileContext cc) throws QueryException {
    if(allAreValues() && exprs[0].size() < FnForEach.UNROLL_LIMIT) {
      // unroll the loop
      cc.info(QueryText.OPTUNROLL_X, this);
      final Value seq = (Value) exprs[0];
      Expr e = exprs[1];
      for(final Item it : seq) {
        e = new DynFuncCall(info, sc, exprs[2], e, it).optimize(cc);
      }
      return e;
    }
    return this;
  }
}
