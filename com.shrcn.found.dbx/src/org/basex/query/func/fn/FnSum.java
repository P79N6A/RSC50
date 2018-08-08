package org.basex.query.func.fn;

import static org.basex.query.QueryError.RANGE_X;

import java.math.BigInteger;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Calc;
import org.basex.query.expr.Expr;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.ANum;
import org.basex.query.value.item.Dur;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;
import org.basex.query.value.seq.RangeSeq;
import org.basex.query.value.type.Type;
import org.basex.query.var.VarRef;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnSum extends Aggr {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    // partial sum calculation (Little Gauss)
    if(exprs[0] instanceof RangeSeq) {
      final RangeSeq rs = (RangeSeq) exprs[0];
      final long s = rs.start(), e = s + rs.size() - 1;
      // range is small enough to be computed with long values
      if(e < 3037000500L) return Int.get((s + e) * (e - s + 1) / 2);
      // compute larger ranges
      final BigInteger bs = BigInteger.valueOf(s), be = BigInteger.valueOf(e);
      final BigInteger bi = bs.add(be).multiply(be.subtract(bs).add(BigInteger.ONE)).
          divide(BigInteger.valueOf(2));
      final long l = bi.longValue();
      // check if result is small enough to be represented as long value
      if(bi.equals(BigInteger.valueOf(l))) return Int.get(l);
      throw RANGE_X.get(info, bi);
    }

    final Iter iter = exprs[0].atomIter(qc, info);
    final Item it = iter.next();
    if(it != null) return sum(iter, it, false);

    // return default item
    return exprs.length == 2 ? exprs[1].atomItem(qc, info) : Int.get(0);
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    final Expr e1 = exprs[0], e2 = exprs.length == 2 ? exprs[1] : null;
    final Type st1 = e1.seqType().type, st2 = e2 != null ? e2.seqType().type : st1;
    if(st1.isNumberOrUntyped() && st2.isNumberOrUntyped()) seqType = Calc.type(st1, st2).seqType();

    // pre-evaluate 0 results (skip non-deterministic and variable expressions)
    final long c = e1.size();
    return c != 0 || e1.has(Flag.NDT) || e1.has(Flag.UPD) || e1 instanceof VarRef ? this :
      e2 instanceof ANum || e2 instanceof Dur ? e2 : e2 != null ? this : Int.get(0);
  }
}
