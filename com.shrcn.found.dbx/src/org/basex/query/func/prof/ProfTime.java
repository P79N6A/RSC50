package org.basex.query.func.prof;

import static org.basex.util.Token.token;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.func.fn.FnTrace;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.util.Performance;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ProfTime extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    // create timer
    final Performance p = new Performance();

    // optional message
    final byte[] msg = exprs.length > 2 ? toToken(exprs[2], qc) : null;

    // check caching flag
    if(exprs.length > 1 && toBoolean(exprs[1], qc)) {
      final Value v = qc.value(exprs[0]).cache().value();
      FnTrace.trace(token(p.getTime()), msg, qc);
      return v.iter();
    }

    return new Iter() {
      final Iter ir = exprs[0].iter(qc);
      @Override
      public Item next() throws QueryException {
        final Item it = ir.next();
        if(it == null) FnTrace.trace(token(p.getTime()), msg, qc);
        return it;
      }
    };
  }
}
