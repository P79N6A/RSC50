package org.basex.query.func.fn;

import org.basex.core.locks.DBLocking;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.util.ASTVisitor;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnLast extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    ctxValue(qc);
    return Int.get(qc.size);
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return visitor.lock(DBLocking.CONTEXT) && super.accept(visitor);
  }
}
