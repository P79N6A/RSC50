package org.basex.query.func.db;

import static org.basex.query.QueryError.BASX_DBTRANSFORM;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.up.TransformModifier;
import org.basex.query.up.Updates;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbOutput extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Updates updates = qc.updates();
    if(updates.mod instanceof TransformModifier) throw BASX_DBTRANSFORM.get(info);
    final Iter iter = qc.iter(exprs[0]);
    for(Item it; (it = iter.next()) != null;) {
      qc.checkStop();
      qc.updates.cache.add(it);
    }
    return null;
  }
}
