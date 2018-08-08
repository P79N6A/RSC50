package org.basex.query.func.fn;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.query.value.seq.Seq;
import org.basex.query.value.type.SeqType;
import org.basex.query.value.type.SeqType.Occ;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnRemove extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final long p = toLong(exprs[1], qc) - 1;
    final Iter ir = exprs[0].iter(qc);
    final long is = ir.size();
    return p < 0 || is != -1 && p > is ? ir : new Iter() {
      long c;
      @Override
      public Item next() throws QueryException {
        return c++ != p || ir.next() != null ? ir.next() : null;
      }
    };
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final Value val = qc.value(exprs[0]);
    final long pos = toLong(exprs[1], qc) - 1, n = val.size();
    if(pos < 0 || pos >= n) return val;
    if(pos == 0 || pos + 1 == n) return val.subSeq(pos == 0 ? 1 : 0, n - 1);
    return ((Seq) val).remove(pos);
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    final SeqType st = exprs[0].seqType();
    seqType = SeqType.get(st.type, st.zeroOrOne() ? Occ.ZERO_ONE : Occ.ZERO_MORE);
    return this;
  }
}
