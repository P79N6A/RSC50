package org.basex.query.func.fn;

import java.util.ListIterator;

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
import org.basex.query.value.seq.tree.TreeSeq;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnFoldRight extends StandardFunc {
  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final Value v = qc.value(exprs[0]);
    Value res = qc.value(exprs[1]);
    final FItem fun = checkArity(exprs[2], 2, qc);
    if(v instanceof TreeSeq) {
      final ListIterator<Item> iter = ((TreeSeq) v).iterator(v.size());
      while(iter.hasPrevious()) res = fun.invokeValue(qc, info, iter.previous(), res);
    } else {
      for(long i = v.size(); --i >= 0;) res = fun.invokeValue(qc, info, v.itemAt(i), res);
    }
    return res;
  }

  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final Value v = qc.value(exprs[0]);
    final FItem fun = checkArity(exprs[2], 2, qc);

    // evaluate start value lazily if it's passed straight through
    if(v.isEmpty()) return exprs[1].iter(qc);

    Value res = qc.value(exprs[1]);
    if(v instanceof TreeSeq) {
      final ListIterator<Item> iter = ((TreeSeq) v).iterator(v.size());
      while(iter.hasPrevious()) res = fun.invokeValue(qc, info, iter.previous(), res);
    } else {
      for(long i = v.size(); --i >= 0;) res = fun.invokeValue(qc, info, v.itemAt(i), res);
    }
    return res.iter();
  }

  @Override
  protected Expr opt(final CompileContext cc) throws QueryException {
    if(allAreValues() && exprs[0].size() < FnForEach.UNROLL_LIMIT) {
      // unroll the loop
      cc.info(QueryText.OPTUNROLL_X, this);
      final Value seq = (Value) exprs[0];
      Expr e = exprs[1];
      for(int i = (int) seq.size(); --i >= 0;) {
        e = new DynFuncCall(info, sc, exprs[2], seq.itemAt(i), e).optimize(cc);
      }
      return e;
    }
    return this;
  }
}
