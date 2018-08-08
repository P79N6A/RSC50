package org.basex.query.func.ft;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.util.ft.FTPosData;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FtCount extends FtAccess {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final FTPosData tmp = qc.ftPosData;
    qc.ftPosData = new FTPosData();
    final Iter ir = qc.iter(exprs[0]);
    for(Item it; (it = ir.next()) != null;) toDBNode(it);
    final int s = qc.ftPosData.size();
    qc.ftPosData = tmp;
    return Int.get(s);
  }
}
