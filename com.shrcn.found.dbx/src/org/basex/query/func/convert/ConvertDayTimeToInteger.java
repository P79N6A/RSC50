package org.basex.query.func.convert;

import static org.basex.query.QueryError.INTRANGE_X;

import java.math.BigDecimal;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.ADateDur;
import org.basex.query.value.item.DTDur;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;
import org.basex.query.value.type.AtomType;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ConvertDayTimeToInteger extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final DTDur dur = (DTDur) checkAtomic(exprs[0], qc, AtomType.DTD);
    final BigDecimal ms = dur.sec.multiply(BigDecimal.valueOf(1000));
    if(ms.compareTo(ADateDur.BDMAXLONG) > 0) throw INTRANGE_X.get(info, ms);
    return Int.get(ms.longValue());
  }
}
