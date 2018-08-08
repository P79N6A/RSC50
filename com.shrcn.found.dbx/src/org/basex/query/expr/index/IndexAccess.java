package org.basex.query.expr.index;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Simple;
import org.basex.query.iter.NodeIter;
import org.basex.query.util.IndexContext;
import org.basex.util.InputInfo;

/**
 * This abstract class retrieves values from an index.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public abstract class IndexAccess extends Simple {
  /** Index context. */
  final IndexContext ictx;

  /**
   * Constructor.
   * @param ictx index context
   * @param info input info
   */
  IndexAccess(final IndexContext ictx, final InputInfo info) {
    super(info);
    this.ictx = ictx;
  }

  /**
   * Sets the number of results.
   * @param s number of results
   */
  public void size(final long s) {
    size = s;
    seqType = seqType().withSize(s);
  }

  @Override
  public abstract NodeIter iter(final QueryContext qc) throws QueryException;

  @Override
  public final boolean iterable() {
    return ictx.iterable || seqType().zeroOrOne();
  }
}
