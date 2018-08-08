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
public final class OutTab extends StandardFunc {
  /** Tab character. */
  private static final Str TAB = Str.get("\t");

  @Override
  public Item item(final QueryContext qc, final InputInfo ii) {
    return TAB;
  }
}
