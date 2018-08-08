package org.basex.query.func.fn;

import static org.basex.query.QueryError.FISTRING_X;

import org.basex.core.locks.DBLocking;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.util.ASTVisitor;
import org.basex.query.value.item.FItem;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.Token;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnStringLength extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] s;
    if(exprs.length == 0) {
      final Item it = ctxValue(qc).item(qc, info);
      if(it instanceof FItem) throw FISTRING_X.get(info, it.type);
      s = it == null ? Token.EMPTY : it.string(info);
    } else {
      s = toEmptyToken(ctxArg(0, qc), qc);
    }
    return Int.get(Token.length(s));
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
