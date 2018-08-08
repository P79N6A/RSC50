package org.basex.query.func.map;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.type.MapType;
import org.basex.query.value.type.SeqType;
import org.basex.query.value.type.SeqType.Occ;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public final class MapGet extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    return toMap(exprs[0], qc).get(toAtomItem(exprs[1], qc), info);
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    final SeqType st = exprs[0].seqType();
    if(st.type instanceof MapType) {
      final SeqType rt = ((MapType) st.type).type;
      if(rt != null) {
        // map lookup may result in empty sequence
        seqType = rt.mayBeZero() ? rt : rt.withOcc(rt.one() ? Occ.ZERO_ONE : Occ.ZERO_MORE);
      }
    }
    return this;
  }
}
