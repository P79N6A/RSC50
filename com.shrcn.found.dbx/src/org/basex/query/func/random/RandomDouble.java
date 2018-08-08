package org.basex.query.func.random;

import java.util.Random;

import org.basex.query.QueryContext;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Dbl;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Dirk Kirsten
 */
public final class RandomDouble extends StandardFunc {
  /** Random instance. */
  private static final Random RND = new Random();

  @Override
  public Item item(final QueryContext qc, final InputInfo ii) {
    return Dbl.get(RND.nextDouble());
  }
}
