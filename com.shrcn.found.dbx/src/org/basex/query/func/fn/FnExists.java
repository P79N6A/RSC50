package org.basex.query.func.fn;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryText;
import org.basex.query.expr.Expr;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Bln;
import org.basex.query.value.item.Item;
import org.basex.query.value.type.NodeType;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnExists extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return Bln.get(exprs[0].iter(qc).next() != null);
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    // ignore non-deterministic expressions (e.g.: error())
    final Expr e = exprs[0];
    return e.size() == -1 || e.has(Flag.NDT) || e.has(Flag.UPD) ? this : Bln.get(e.size() != 0);
  }

  @Override
  public Expr optimizeEbv(final CompileContext cc) {
    // if(exists(node*)) -> if(node*)
    final Expr e = exprs[0];
    if(e.seqType().type instanceof NodeType) {
      cc.info(QueryText.OPTREWRITE_X, this);
      return e;
    }
    return this;
  }
}
