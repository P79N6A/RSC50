package org.basex.query.func.prof;

import static org.basex.util.Token.token;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.func.StandardFunc;
import org.basex.query.func.fn.FnTrace;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.util.Util;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ProfType extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    return type(qc).value(qc);
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    return type(cc.qc);
  }

  /**
   * Dumps the argument's type and size and returns it unchanged.
   * @param qc query context
   * @return the argument expression
   */
  private Expr type(final QueryContext qc) {
    FnTrace.trace(Util.inf("{ type: %, size: %, exprSize: % }", exprs[0].seqType(), exprs[0].size(),
        exprs[0].exprSize()), token(exprs[0].toString()), qc);
    return exprs[0];
  }
}
