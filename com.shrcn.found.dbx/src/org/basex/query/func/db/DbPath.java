package org.basex.query.func.db;

import org.basex.data.Data;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Str;
import org.basex.query.value.node.ANode;
import org.basex.query.value.node.DBNode;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbPath extends DbAccess {
  @Override
  public Str item(final QueryContext qc, final InputInfo ii) throws QueryException {
    ANode node, par = toNode(exprs[0], qc);
    do {
      node = par;
      par = node.parent();
    } while(par != null);
    final DBNode dbn = toDBNode(node);
    return dbn.kind() == Data.DOC ? Str.get(dbn.data().text(dbn.pre(), true)) : Str.ZERO;
  }
}
