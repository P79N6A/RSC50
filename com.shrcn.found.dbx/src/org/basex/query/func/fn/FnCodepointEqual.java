package org.basex.query.func.fn;

import static org.basex.util.Token.eq;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
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
public final class FnCodepointEqual extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Item it1 = exprs[0].atomItem(qc, info), it2 = exprs[1].atomItem(qc, info);
    return it1 == null || it2 == null ? null : Bln.get(eq(toToken(it1), toToken(it2)));
  }
}
