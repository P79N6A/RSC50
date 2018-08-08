package org.basex.query.func.unit;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class UnitAssert extends UnitFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final boolean ok = exprs[0].ebv(qc, info).bool(info);
    final Item it = exprs.length < 2 ? null : toNodeOrAtomItem(exprs[1], qc);
    if(ok) return null;
    throw error(it);
  }
}
