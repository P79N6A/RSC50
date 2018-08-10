package org.basex.query.func.bin;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.B64;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.list.ByteList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class BinJoin extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final ByteList bl = new ByteList();
    final Iter ir = exprs[0].atomIter(qc, info);
    for(Item it; (it = ir.next()) != null;) bl.add(toB64(it, true).binary(info));
    return new B64(bl.finish());
  }
}
