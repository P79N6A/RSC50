package org.basex.query.func.fn;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryText;
import org.basex.query.expr.Arr;
import org.basex.query.expr.Cmp;
import org.basex.query.expr.CmpG;
import org.basex.query.expr.CmpV;
import org.basex.query.expr.Expr;
import org.basex.query.func.Function;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Bln;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnNot extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return Bln.get(!exprs[0].ebv(qc, info).bool(info));
  }

  @Override
  protected Expr opt(final CompileContext cc) throws QueryException {
    final Expr e = exprs[0];
    // simplify: not(empty(A)) -> exists(A)
    if(e.isFunction(Function.EMPTY)) {
      cc.info(QueryText.OPTREWRITE_X, this);
      exprs = ((Arr) e).exprs;
      return cc.function(Function.EXISTS, info, exprs);
    }
    // simplify: not(exists(A)) -> empty(A)
    if(e.isFunction(Function.EXISTS)) {
      cc.info(QueryText.OPTREWRITE_X, this);
      exprs = ((Arr) e).exprs;
      return cc.function(Function.EMPTY, info, exprs);
    }
    // simplify: not('a' = 'b') -> 'a' != 'b'
    if(e instanceof CmpV || e instanceof CmpG) {
      final Cmp c = ((Cmp) e).invert();
      return c == e ? this : c;
    }
    // simplify: not(not(A)) -> boolean(A)
    if(e.isFunction(Function.NOT)) {
      return compBln(((Arr) e).exprs[0], info, cc.sc());
    }
    // simplify, e.g.: not(boolean(A)) -> not(A)
    exprs[0] = e.optimizeEbv(cc);
    return this;
  }
}
