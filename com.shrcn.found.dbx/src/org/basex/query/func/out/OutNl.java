package org.basex.query.func.out;

import org.basex.query.QueryContext;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class OutNl extends StandardFunc {
  /** Newline character. */
  private static final Str NL = Str.get("\n");

  @Override
  public Item item(final QueryContext qc, final InputInfo ii) {
    return NL;
  }
}
