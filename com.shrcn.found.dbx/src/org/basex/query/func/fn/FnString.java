package org.basex.query.func.fn;

import static org.basex.query.QueryError.FISTRING_X;

import org.basex.core.locks.DBLocking;
import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.func.StandardFunc;
import org.basex.query.util.ASTVisitor;
import org.basex.query.value.item.FItem;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.query.value.type.AtomType;
import org.basex.query.value.type.SeqType;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnString extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Item it = ctxArg(0, qc).item(qc, info);
    if(it instanceof FItem) throw FISTRING_X.get(info, it.type);
    return it == null ? Str.ZERO : it.type == AtomType.STR ? it : Str.get(it.string(info));
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    // string('x') -> 'x'
    return exprs.length != 0 && exprs[0].seqType().eq(SeqType.STR) ? optPre(exprs[0], cc) : this;
  }

  @Override
  public boolean has(final Flag flag) {
    return flag == Flag.CTX && exprs.length == 0 || super.has(flag);
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return (exprs.length != 0 || visitor.lock(DBLocking.CONTEXT)) && super.accept(visitor);
  }
}
