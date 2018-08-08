package org.basex.query.func.fn;

import static org.basex.util.Token.string;

import java.util.regex.Pattern;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Bln;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnMatches extends RegEx {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] value = toEmptyToken(exprs[0], qc);
    final Pattern p = pattern(exprs[1], exprs.length == 3 ? exprs[2] : null, qc, false);
    return Bln.get(p.matcher(string(value)).find());
  }
}
