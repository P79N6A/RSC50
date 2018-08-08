package org.basex.query.func.fn;

import static org.basex.query.QueryError.FORMNUM_X;
import static org.basex.query.QueryError.numberError;
import static org.basex.util.Token.EMPTY;
import static org.basex.util.Token.XML;
import static org.basex.util.Token.trim;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.util.format.DecFormatter;
import org.basex.query.value.item.ANum;
import org.basex.query.value.item.Dbl;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.QNm;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnFormatNumber extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    // evaluate arguments
    Item it = exprs[0].atomItem(qc, info);
    if(it == null) it = Dbl.NAN;
    else if(it.type.isUntyped()) it = Dbl.get(it.dbl(info));
    else if(!it.type.isNumberOrUntyped()) throw numberError(this, it);

    // retrieve picture
    final byte[] pic = toToken(exprs[1], qc);
    // retrieve format declaration
    final QNm frm = exprs.length == 3 ? new QNm(trim(toEmptyToken(exprs[2], qc)), sc) :
      new QNm(EMPTY);
    final DecFormatter df = sc.decFormat(frm.id());
    if(df == null) throw FORMNUM_X.get(info, frm.prefixId(XML));

    return Str.get(df.format(info, (ANum) it, pic));
  }
}
