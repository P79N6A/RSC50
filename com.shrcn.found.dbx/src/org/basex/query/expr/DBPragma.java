package org.basex.query.expr;

import static org.basex.query.QueryError.BASX_VALUE_X_X;
import static org.basex.util.Token.string;

import org.basex.core.BaseXException;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr.Flag;
import org.basex.query.value.item.QNm;
import org.basex.util.InputInfo;
import org.basex.util.options.Option;

/**
 * Pragma for database options.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public final class DBPragma extends Pragma {
  /** Option key. */
  private final Option<?> option;

  /**
   * Constructor.
   * @param name name of pragma
   * @param option option
   * @param value optional value
   */
  public DBPragma(final QNm name, final Option<?> option, final byte[] value) {
    super(name, value);
    this.option = option;
  }

  @Override
  Object init(final QueryContext qc, final InputInfo info) throws QueryException {
    final Object old = qc.context.options.get(option);
    try {
      qc.context.options.assign(option.name(), string(value));
    } catch(final BaseXException ex) {
      throw BASX_VALUE_X_X.get(info, option.name(), value);
    }
    return old;
  }

  @Override
  void finish(final QueryContext qc, final Object cache) {
    qc.context.options.put(option, cache);
  }

  @Override
  public boolean has(final Flag flag) {
    return false;
  }

  @Override
  public Pragma copy() {
    return new DBPragma(name, option, value);
  }
}
