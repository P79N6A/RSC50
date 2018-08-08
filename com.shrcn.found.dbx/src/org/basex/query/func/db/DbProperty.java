package org.basex.query.func.db;

import static org.basex.query.QueryError.BXDB_PROP_X;
import static org.basex.util.Token.string;

import org.basex.data.Data;
import org.basex.data.MetaProp;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Bln;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbProperty extends DbAccess {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Data data = checkData(qc);
    final String name = string(toToken(exprs[1], qc));
    final MetaProp prop = MetaProp.get(name);
    if(prop == null) throw BXDB_PROP_X.get(info, name);

    final Object value = prop.value(data.meta);
    if(value instanceof Boolean) return Bln.get((Boolean) value);
    if(value instanceof Integer) return Int.get((Integer) value);
    if(value instanceof Long)    return Int.get((Long)    value);
    return Str.get(value.toString());
  }
}
