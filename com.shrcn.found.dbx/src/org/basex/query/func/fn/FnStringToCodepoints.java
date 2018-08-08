package org.basex.query.func.fn;

import static org.basex.util.Token.cl;
import static org.basex.util.Token.cp;
import static org.basex.util.Token.cps;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;
import org.basex.query.value.seq.Empty;
import org.basex.query.value.seq.IntSeq;
import org.basex.query.value.type.AtomType;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnStringToCodepoints extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final byte[] s = toEmptyToken(exprs[0], qc);
    if(s == null) return Empty.ITER;

    return new Iter() {
      int l;
      @Override
      public Item next() {
        if(l >= s.length) return null;
        final int i = cp(s, l);
        l += cl(s, l);
        return Int.get(i);
      }
    };
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final int[] tmp = cps(toEmptyToken(exprs[0], qc));
    final int tl = tmp.length;
    final long[] vals = new long[tl];
    for(int t = 0; t < tl; t++) vals[t] = tmp[t];
    return IntSeq.get(vals, AtomType.ITR);
  }
}
