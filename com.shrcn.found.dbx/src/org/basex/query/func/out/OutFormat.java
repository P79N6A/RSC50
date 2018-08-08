package org.basex.query.func.out;

import static org.basex.query.QueryError.ERRFORMAT_X_X;
import static org.basex.util.Token.string;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;
import org.basex.util.Util;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class OutFormat extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final String form = string(toToken(exprs[0], qc));
    final int es = exprs.length;
    final Object[] args = new Object[es - 1];
    for(int e = 1; e < es; e++) {
      final Item it = exprs[e].item(qc, info);
      args[e - 1] = it == null ? null : it.type.isUntyped() ? string(it.string(info)) : it.toJava();
    }
    try {
      return Str.get(String.format(form, args));
    } catch(final RuntimeException ex) {
      throw ERRFORMAT_X_X.get(info, Util.className(ex), ex);
    }
  }
}
