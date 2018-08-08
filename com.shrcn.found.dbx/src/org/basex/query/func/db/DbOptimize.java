package org.basex.query.func.db;

import org.basex.data.Data;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.up.primitives.db.DBOptimize;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.options.Options;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbOptimize extends DbNew {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Data data = checkData(qc);
    final boolean all = exprs.length > 1 && toBoolean(exprs[1], qc);
    final Options opts = toOptions(2, new Options(), qc);
    qc.updates().add(new DBOptimize(data, all, opts, qc, info), qc);
    return null;
  }
}
