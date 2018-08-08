package org.basex.query.func.fn;

import static org.basex.util.Token.diff;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.util.collation.Collation;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnCompare extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Item it1 = exprs[0].atomItem(qc, info), it2 = exprs[1].atomItem(qc, info);
    final Collation coll = toCollation(2, qc);
    if(it1 == null || it2 == null) return null;
    return Int.get(Math.max(-1, Math.min(1, coll == null ? diff(toToken(it1), toToken(it2)) :
      coll.compare(toToken(it1), toToken(it2)))));
  }
}
