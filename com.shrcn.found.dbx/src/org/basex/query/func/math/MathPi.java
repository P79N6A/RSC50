package org.basex.query.func.math;

import static java.lang.StrictMath.PI;

import org.basex.query.QueryContext;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Dbl;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class MathPi extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) {
    return Dbl.get(PI);
  }
}
