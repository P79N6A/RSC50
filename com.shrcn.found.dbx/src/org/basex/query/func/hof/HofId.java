package org.basex.query.func.hof;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public class HofId extends StandardFunc {
  @Override
  public final Iter iter(final QueryContext qc) throws QueryException {
    return qc.iter(exprs[0]);
  }

  @Override
  public final Value value(final QueryContext qc) throws QueryException {
    return qc.value(exprs[0]);
  }

  @Override
  public final Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return exprs[0].item(qc, info);
  }

  @Override
  protected final Expr opt(final CompileContext cc) {
    return exprs[0];
  }
}
