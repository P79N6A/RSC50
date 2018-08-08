package org.basex.query.func.fn;

import static org.basex.util.Token.EMPTY;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;
import org.basex.util.TokenBuilder;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnStringJoin extends StandardFunc {
  @Override
  public Str item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Iter iter = exprs[0].atomIter(qc, info);
    final byte[] token = exprs.length == 2 ? toToken(exprs[1], qc) : EMPTY;

    // no results: empty string
    Item it = iter.next();
    if(it == null) return Str.ZERO;

    // single result
    final byte[] first = it.string(info);
    if((it = iter.next()) == null) return Str.get(first);

    // join multiple strings
    final TokenBuilder tb = new TokenBuilder(first);
    do {
      tb.add(token).add(it.string(info));
    } while((it = iter.next()) != null);
    return Str.get(tb.finish());
  }
}
