package org.basex.query.func.fn;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryText;
import org.basex.query.expr.Expr;
import org.basex.query.expr.List;
import org.basex.query.func.DynFuncCall;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.FItem;
import org.basex.query.value.item.Item;
import org.basex.query.value.seq.Empty;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnForEach extends StandardFunc {
  /** Minimum size of a loop that should not be unrolled. */
  public static final int UNROLL_LIMIT = 10;

  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final FItem f = checkArity(exprs[1], 1, qc);
    final Iter ir = exprs[0].iter(qc);
    return new Iter() {
      Iter ir2 = Empty.ITER;

      @Override
      public Item next() throws QueryException {
        do {
          final Item it = ir2.next();
          if(it != null) return it;
          final Item it2 = ir.next();
          if(it2 == null) return null;
          ir2 = f.invokeValue(qc, info, it2).iter();
        } while(true);
      }
    };
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final FItem f = checkArity(exprs[1], 1, qc);
    final Iter iter = exprs[0].iter(qc);
    Item it = iter.next();
    if(it == null) return Empty.SEQ;
    final Value v1 = f.invokeValue(qc, info, it);
    it = iter.next();
    if(it == null) return v1;

    final ValueBuilder vb = new ValueBuilder().add(v1);
    do {
      vb.add(f.invokeValue(qc, info, it));
    } while((it = iter.next()) != null);
    return vb.value();
  }

  @Override
  protected Expr opt(final CompileContext cc) throws QueryException {
    if(allAreValues() && exprs[0].size() < UNROLL_LIMIT) {
      // unroll the loop
      cc.info(QueryText.OPTUNROLL_X, this);
      final Value seq = (Value) exprs[0];
      final int len = (int) seq.size();

      // fn:for-each(...)
      final Expr[] results = new Expr[len];
      for(int i = 0; i < len; i++) {
        results[i] = new DynFuncCall(info, sc, exprs[1], seq.itemAt(i)).optimize(cc);
      }
      return new List(info, results).optimize(cc);
    }
    return this;
  }
}
