package org.basex.query.func.fn;

import static org.basex.util.Token.indexOf;
import static org.basex.util.Token.substring;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.util.collation.Collation;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnSubstringAfter extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] ss = toEmptyToken(exprs[0], qc), sb = toEmptyToken(exprs[1], qc);
    final Collation coll = toCollation(2, qc);
    if(coll == null) {
      final int p = indexOf(ss, sb);
      return p == -1 ? Str.ZERO : Str.get(substring(ss, p + sb.length));
    }
    return Str.get(coll.after(ss, sb, info));
  }
}
