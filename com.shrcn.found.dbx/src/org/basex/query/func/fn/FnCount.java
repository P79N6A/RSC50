package org.basex.query.func.fn;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.func.Function;
import org.basex.query.func.StandardFunc;
import org.basex.query.func.map.MapKeys;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;
import org.basex.query.var.VarRef;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnCount extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Iter iter = qc.iter(exprs[0]);
    long c = iter.size();
    if(c == -1) {
      do {
        qc.checkStop();
        ++c;
      } while(iter.next() != null);
    }
    return Int.get(c);
  }

  @Override
  protected Expr opt(final CompileContext cc) throws QueryException {
    // skip non-deterministic and variable expressions
    final Expr e = exprs[0];
    if(e.has(Flag.NDT) || e.has(Flag.UPD) || e instanceof VarRef) return this;

    final long c = e.size();
    if(c >= 0) return Int.get(c);

    if(e instanceof MapKeys) {
      return cc.function(Function._MAP_SIZE, info, ((MapKeys) e).exprs);
    }
    return this;
  }
}
