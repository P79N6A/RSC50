package org.basex.query.func.fn;

import static org.basex.query.QueryError.FUNERR1;
import static org.basex.query.func.Function.ERROR;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.StaticContext;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.QNm;
import org.basex.query.value.item.Str;
import org.basex.query.value.type.SeqType;
import org.basex.util.Token;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnError extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final int al = exprs.length;
    if(al == 0) throw FUNERR1.get(info);

    QNm name = toQNm(exprs[0], qc, true);
    if(name == null) name = FUNERR1.qname();

    String msg = FUNERR1.desc;
    if(al > 1) msg = Token.string(toEmptyToken(exprs[1], qc));
    final Value val = al > 2 ? qc.value(exprs[2]) : null;
    throw new QueryException(info, name, msg).value(val);
  }

  @Override
  public boolean isVacuous() {
    return true;
  }

  /**
   * Creates an error function instance.
   * @param ex query exception
   * @param st type of the expression
   * @param sc static context
   * @return function
   */
  public static StandardFunc get(final QueryException ex, final SeqType st,
      final StaticContext sc) {
    final StandardFunc sf = ERROR.get(sc, ex.info(), ex.qname(), Str.get(ex.getLocalizedMessage()));
    sf.seqType = st;
    return sf;
  }
}
