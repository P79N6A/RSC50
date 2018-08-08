package org.basex.query.func.prof;

import static org.basex.util.Token.token;

import org.basex.query.QueryContext;
import org.basex.query.func.StandardFunc;
import org.basex.query.func.fn.FnTrace;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ProfVariables extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) {
    FnTrace.trace(token(qc.stack.dump()), null, qc);
    return null;
  }
}
