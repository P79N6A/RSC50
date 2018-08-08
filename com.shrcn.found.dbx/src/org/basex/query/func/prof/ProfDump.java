package org.basex.query.func.prof;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.func.fn.FnTrace;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ProfDump extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Iter ir = exprs[0].iter(qc);
    final byte[] label = exprs.length > 1 ? toToken(exprs[1], qc) : null;
    boolean empty = true;
    for(Item it; (it = ir.next()) != null;) {
      FnTrace.trace(it, label, info, qc);
      empty = false;
    }
    if(empty) FnTrace.trace(null, label, info, qc);
    return null;
  }
}
