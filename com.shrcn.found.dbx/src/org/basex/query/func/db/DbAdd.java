package org.basex.query.func.db;

import static org.basex.util.Token.EMPTY;
import static org.basex.util.Token.token;

import org.basex.data.Data;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.up.primitives.NewInput;
import org.basex.query.up.primitives.db.DBAdd;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.options.Options;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbAdd extends DbNew {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Data data = checkData(qc);
    final byte[] path = exprs.length < 3 ? EMPTY : token(path(2, qc));
    final NewInput input = checkInput(toNodeOrAtomItem(exprs[1], qc), path);
    final Options opts = toOptions(3, new Options(), qc);
    qc.updates().add(new DBAdd(data, input, opts, false, qc, info), qc);
    return null;
  }
}
