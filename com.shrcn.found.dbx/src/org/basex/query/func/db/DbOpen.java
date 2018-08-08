package org.basex.query.func.db;

import org.basex.data.Data;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.seq.DBNodeSeq;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbOpen extends DbAccess {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final Data data = checkData(qc);
    final String path = exprs.length < 2 ? "" : path(1, qc);
    return DBNodeSeq.get(data.resources.docs(path), data, true, path.isEmpty());
  }

  @Override
  public boolean iterable() {
    return true;
  }
}
