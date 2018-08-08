package org.basex.query.func.util;

import static org.basex.query.QueryError.INVALIDOPTION_X;
import static org.basex.util.Token.eq;
import static org.basex.util.Token.token;
import static org.basex.util.Token.uc;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.func.fn.DeepEqual;
import org.basex.query.func.fn.DeepEqual.Mode;
import org.basex.query.value.item.Bln;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class UtilDeepEqual extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final DeepEqual cmp = new DeepEqual(info);
    final Mode[] modes = Mode.values();
    if(exprs.length == 3) {
      for(final Item it : exprs[2].atomValue(qc, info)) {
        final byte[] key = uc(toToken(it));
        boolean found = false;
        for(final Mode m : modes) {
          found = eq(key, token(m.name()));
          if(found) {
            cmp.flag(m);
            break;
          }
        }
        if(!found) throw INVALIDOPTION_X.get(info, key);
      }
    }
    return Bln.get(cmp.equal(qc.iter(exprs[0]), qc.iter(exprs[1])));
  }
}
