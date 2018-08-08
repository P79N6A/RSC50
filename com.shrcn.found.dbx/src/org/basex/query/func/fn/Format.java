package org.basex.query.func.fn;

import static org.basex.util.Token.EMPTY;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.Functions;
import org.basex.query.func.StandardFunc;
import org.basex.query.util.format.Formatter;
import org.basex.query.value.item.ADate;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.query.value.type.AtomType;
import org.basex.util.list.IntList;

/**
 * Formatting functions.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
abstract class Format extends StandardFunc {
  /**
   * Returns a formatted number.
   * @param qc query context
   * @param tp input type
   * @return string
   * @throws QueryException query exception
   */
  Item formatDate(final AtomType tp, final QueryContext qc) throws QueryException {
    final int el = exprs.length;
    if(el == 3 || el == 4) throw Functions.wrongArity(sig, el, new IntList(), info);

    final Item it = exprs[0].atomItem(qc, info);
    final byte[] pic = toEmptyToken(exprs[1], qc);
    final byte[] lng = el == 5 ? toEmptyToken(exprs[2], qc) : EMPTY;
    final byte[] cal = el == 5 ? toEmptyToken(exprs[3], qc) : EMPTY;
    final byte[] plc = el == 5 ? toEmptyToken(exprs[4], qc) : EMPTY;
    if(it == null) return null;

    final ADate date = (ADate) checkType(it, tp);
    final Formatter form = Formatter.get(lng);
    return Str.get(form.formatDate(date, lng, pic, cal, plc, info, sc));
  }
}
