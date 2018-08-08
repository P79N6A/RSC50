package org.basex.query.func.fn;

import static org.basex.query.QueryError.ONEORMORE;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.query.value.type.SeqType;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnOneOrMore extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final Iter ir = exprs[0].iter(qc);
    final long len = ir.size();
    if(len == 0) throw ONEORMORE.get(info);
    if(len > 0) return ir;
    return new Iter() {
      private boolean first = true;
      @Override
      public Item next() throws QueryException {
        final Item it = ir.next();
        if(first) {
          if(it == null) throw ONEORMORE.get(info);
          first = false;
        }
        return it;
      }
    };
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final Value val = qc.value(exprs[0]);
    if(val.isEmpty()) throw ONEORMORE.get(info);
    return val;
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    final Expr e = exprs[0];
    final SeqType st = e.seqType();
    if(!st.mayBeZero()) return e;
    seqType = SeqType.get(st.type, seqType.occ);
    return this;
  }
}
