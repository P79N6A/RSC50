package org.basex.query.func.fn;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.iter.ValueIter;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.query.value.seq.Empty;
import org.basex.query.value.type.SeqType;
import org.basex.query.value.type.SeqType.Occ;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnTail extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final Expr e = exprs[0];
    if(e.seqType().zeroOrOne()) return Empty.ITER;

    final Iter ir = e.iter(qc);
    if(ir instanceof ValueIter) {
      final Value val = ir.value();
      return val.size() < 2 ? Empty.ITER : val.subSeq(1, val.size() - 1).iter();
    }
    if(ir.next() == null) return Empty.ITER;

    return new Iter() {
      @Override
      public Item next() throws QueryException {
        return ir.next();
      }
    };
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final Value val = qc.value(exprs[0]);
    return val.size() < 2 ? Empty.SEQ : val.subSeq(1, val.size() - 1);
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    final SeqType st = exprs[0].seqType();
    seqType = st.zeroOrOne() ? SeqType.EMP : SeqType.get(st.type, Occ.ZERO_MORE);
    return this;
  }
}
