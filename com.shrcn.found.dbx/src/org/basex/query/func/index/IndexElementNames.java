package org.basex.query.func.index;

import static org.basex.util.Token.EMPTY;

import org.basex.data.Data;
import org.basex.index.IndexType;
import org.basex.index.query.IndexEntries;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class IndexElementNames extends IndexFn {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return names(qc, IndexType.ELEMNAME);
  }

  /**
   * Returns all entries of the specified name index.
   * @param qc query context
   * @param it index type
   * @return text entries
   * @throws QueryException query exception
   */
  Iter names(final QueryContext qc, final IndexType it) throws QueryException {
    final Data data = checkData(qc);
    return entries(it == IndexType.ELEMNAME ? data.elemNames : data.attrNames,
      new IndexEntries(EMPTY, it));
  }
}
