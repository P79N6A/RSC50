package org.basex.query.func.fn;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnUnordered extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return exprs[0].iter(qc);
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    return exprs[0];
  }
}
