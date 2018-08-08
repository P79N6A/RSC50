package org.basex.query.func.fn;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.util.collation.Collation;
import org.basex.query.value.item.Bln;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.Token;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnContains extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] ss = toEmptyToken(exprs[0], qc), sb = toEmptyToken(exprs[1], qc);
    final Collation coll = toCollation(2, qc);
    return Bln.get(coll == null ? Token.contains(ss, sb) : coll.contains(ss, sb, info));
  }
}
