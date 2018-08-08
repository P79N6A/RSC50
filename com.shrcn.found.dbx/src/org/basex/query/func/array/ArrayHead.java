package org.basex.query.func.array;

import org.basex.query.QueryContext;
import org.basex.query.QueryError;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.array.Array;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ArrayHead extends ArrayFn {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final Array array = toArray(exprs[0], qc);
    if(array.isEmptyArray()) throw QueryError.ARRAYEMPTY.get(info);
    return array.head();
  }
}
