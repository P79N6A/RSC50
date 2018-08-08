package org.basex.query.func.fn;

import static org.basex.query.QueryError.REGROUP;
import static org.basex.util.Token.normalize;
import static org.basex.util.Token.split;
import static org.basex.util.Token.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.seq.StrSeq;
import org.basex.util.list.TokenList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnTokenize extends RegEx {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final byte[] val = toEmptyToken(exprs[0], qc);
    if(exprs.length < 2) return StrSeq.get(split(normalize(val), ' '));

    final Pattern p = pattern(exprs[1], exprs.length == 3 ? exprs[2] : null, qc, true);
    if(p.matcher("").matches()) throw REGROUP.get(info);

    final TokenList tl = new TokenList();
    final String str = string(val);
    if(!str.isEmpty()) {
      final Matcher m = p.matcher(str);
      int s = 0;
      while(m.find()) {
        tl.add(str.substring(s, m.start()));
        s = m.end();
      }
      tl.add(str.substring(s, str.length()));
    }
    return StrSeq.get(tl);
  }

  /**
   * Returns the input argument if no others are specified, and if it returns at most one item.
   * @return first argument
   */
  public Expr input() {
    // X must yield single result (otherwise, it may result in an error)
    return exprs.length == 1 && exprs[0].seqType().zeroOrOne() ? exprs[0] : null;
  }
}
