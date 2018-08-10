package org.basex.query.func.bin;

import static org.basex.query.QueryError.BIN_OOR_X;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.B64;
import org.basex.query.value.item.Item;
import org.basex.util.Array;
import org.basex.util.InputInfo;
import org.basex.util.list.ByteList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class BinFromOctets extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Iter ir = exprs[0].atomIter(qc, info);
    final ByteList bl = new ByteList(Math.max(Array.CAPACITY, (int) ir.size()));
    for(Item it; (it = ir.next()) != null;) {
      final long l = toLong(it);
      if(l < 0 || l > 255) throw BIN_OOR_X.get(info, l);
      bl.add((int) l);
    }
    return new B64(bl.finish());
  }
}
