package org.basex.query.func.prof;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ProfVoid extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Iter ir = exprs[0].iter(qc);
    // materialize values to ensure that streams are consumed
    for(Item it; (it = ir.next()) != null;) it.materialize(info);
    return null;
  }
}
