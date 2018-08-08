package org.basex.query.func.inspect;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.ann.Ann;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.Item;
import org.basex.query.value.map.Map;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class InspectFunctionAnnotations extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    Map map = Map.EMPTY;
    for(final Ann ann : toFunc(exprs[0], qc).annotations()) {
      final ValueBuilder vb = new ValueBuilder();
      for(final Item item : ann.args()) vb.add(item);
      map = map.put(ann.name(), vb.value(), info);
    }
    return map;
  }
}
