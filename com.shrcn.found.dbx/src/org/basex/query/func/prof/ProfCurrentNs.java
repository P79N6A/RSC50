package org.basex.query.func.prof;

import org.basex.query.QueryContext;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ProfCurrentNs extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) {
    return Int.get(System.nanoTime());
  }
}
