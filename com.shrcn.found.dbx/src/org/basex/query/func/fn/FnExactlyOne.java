package org.basex.query.func.fn;

import static org.basex.query.QueryError.EXACTLYONE;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Item;
import org.basex.query.value.type.SeqType;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnExactlyOne extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Iter ir = exprs[0].iter(qc);
    final Item it = ir.next();
    if(it == null || ir.next() != null) throw EXACTLYONE.get(info);
    return it;
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    final Expr e = exprs[0];
    final SeqType st = e.seqType();
    if(st.one()) return e;
    seqType = SeqType.get(st.type, seqType.occ);
    return this;
  }
}
