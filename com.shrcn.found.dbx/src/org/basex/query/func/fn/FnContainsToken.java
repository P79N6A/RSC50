package org.basex.query.func.fn;

import static org.basex.util.Token.distinctTokens;
import static org.basex.util.Token.eq;
import static org.basex.util.Token.trim;

import org.basex.index.IndexType;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.util.IndexInfo;
import org.basex.query.util.collation.Collation;
import org.basex.query.value.item.Bln;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnContainsToken extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] token = trim(toToken(exprs[1], qc));
    final Collation coll = toCollation(2, qc);
    if(token.length != 0) {
      final Iter ir = qc.iter(exprs[0]);
      for(Item it; (it = ir.next()) != null;) {
        for(final byte[] tok : distinctTokens(toToken(it))) {
          if(coll == null ? eq(token, tok) : coll.compare(token, tok) == 0) return Bln.TRUE;
        }
      }
    }
    return Bln.FALSE;
  }

  @Override
  public boolean indexAccessible(final IndexInfo ii) throws QueryException {
    // support limited to default collation
    return exprs.length == 2 &&
           ii.create(input(), ii.type(exprs[0], IndexType.TOKEN), info, true);
  }

  /**
   * Returns the input argument if no others are specified, and if it returns at most one item.
   * @return first argument
   */
  private Expr input() {
    return exprs.length == 2 && exprs[1].seqType().zeroOrOne() ? exprs[1] : null;
  }
}
