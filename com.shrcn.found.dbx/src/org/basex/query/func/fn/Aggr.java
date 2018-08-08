package org.basex.query.func.fn;

import static org.basex.query.QueryError.SUMDUR_X_X;
import static org.basex.query.QueryError.SUMNUM_X_X;
import static org.basex.query.QueryError.SUM_X_X;
import static org.basex.query.value.type.AtomType.DTD;
import static org.basex.query.value.type.AtomType.YMD;

import org.basex.query.QueryException;
import org.basex.query.expr.Calc;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.ANum;
import org.basex.query.value.item.Dbl;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;

/**
 * Aggregation function.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
abstract class Aggr extends StandardFunc {
  /**
   * Sums up the specified item(s).
   * @param iter iterator
   * @param it first item
   * @param avg calculate average
   * @return summed up item
   * @throws QueryException query exception
   */
  Item sum(final Iter iter, final Item it, final boolean avg) throws QueryException {
    Item rs = it.type.isUntyped() ? Dbl.get(it.dbl(info)) : it;
    final boolean num = rs instanceof ANum, dtd = rs.type == DTD, ymd = rs.type == YMD;
    if(!num && !dtd && !ymd) throw SUM_X_X.get(info, rs.type, rs);

    int c = 1;
    for(Item i; (i = iter.next()) != null;) {
      if(i.type.isNumberOrUntyped()) {
        if(!num) throw SUMDUR_X_X.get(info, i.type, i);
      } else {
        if(num) throw SUMNUM_X_X.get(info, i.type, i);
        if(dtd && i.type != DTD || ymd && i.type != YMD) throw SUMDUR_X_X.get(info, i.type, i);
      }
      rs = Calc.PLUS.ev(rs, i, info);
      ++c;
    }
    return avg ? Calc.DIV.ev(rs, Int.get(c), info) : rs;
  }
}
