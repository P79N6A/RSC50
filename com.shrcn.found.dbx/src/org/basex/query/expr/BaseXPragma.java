package org.basex.query.expr;

import org.basex.query.QueryContext;
import org.basex.query.QueryText;
import org.basex.query.expr.Expr.Flag;
import org.basex.query.value.item.QNm;
import org.basex.util.InputInfo;
import org.basex.util.Token;

/**
 * Pragma for database options.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public final class BaseXPragma extends Pragma {
  /**
   * Constructor.
   * @param name name of pragma
   * @param value optional value
   */
  public BaseXPragma(final QNm name, final byte[] value) {
    super(name, value);
  }

  @Override
  Object init(final QueryContext qc, final InputInfo info) {
    return null;
  }

  @Override
  void finish(final QueryContext qc, final Object cache) {
  }

  @Override
  public boolean has(final Flag flag) {
    return flag == Flag.NDT && Token.eq(name.local(), Token.token(QueryText.NON_DETERMNISTIC));
  }

  @Override
  public Pragma copy() {
    return new BaseXPragma(name, value);
  }
}
