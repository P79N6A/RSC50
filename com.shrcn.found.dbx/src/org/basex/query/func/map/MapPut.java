package org.basex.query.func.map;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.query.value.map.Map;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public final class MapPut extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Map map = toMap(exprs[0], qc);
    final Item key = toAtomItem(exprs[1], qc);
    final Value val = qc.value(exprs[2]);
    return map.put(key, val, info);
  }
}
