package org.basex.query.expr;

import static org.basex.query.QueryError.RANGE_X;
import static org.basex.query.QueryText.TO;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.query.value.seq.Empty;
import org.basex.query.value.seq.RangeSeq;
import org.basex.query.value.type.SeqType;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * Range expression.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Range extends Arr {
  /**
   * Constructor.
   * @param info input info
   * @param expr1 first expression
   * @param expr2 second expression
   */
  public Range(final InputInfo info, final Expr expr1, final Expr expr2) {
    super(info, expr1, expr2);
    seqType = SeqType.ITR_ZM;
  }

  @Override
  public Expr optimize(final CompileContext cc) throws QueryException {
    Expr e = this;
    if(oneIsEmpty()) {
      e = Empty.SEQ;
    } else if(allAreValues()) {
      e = value(cc.qc);
    }
    return optPre(e, cc);
  }

  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final Item it1 = exprs[0].atomItem(qc, info);
    if(it1 == null) return Empty.SEQ;
    final Item it2 = exprs[1].atomItem(qc, info);
    if(it2 == null) return Empty.SEQ;
    final long s = toLong(it1), e = toLong(it2);
    if(s > e) return Empty.SEQ;
    final long n = e - s + 1;
    if(n > 0) return RangeSeq.get(s, n, true);
    throw RANGE_X.get(info, e);
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new Range(info, exprs[0].copy(cc, vm), exprs[1].copy(cc, vm));
  }

  @Override
  public String toString() {
    return toString(' ' + TO + ' ');
  }
}
