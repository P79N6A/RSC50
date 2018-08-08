package org.basex.query.func.db;

import org.basex.core.cmd.Info;
import org.basex.query.QueryContext;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbSystem extends DbFn {
  /** Resource element name. */
  private static final String SYSTEM = "system";

  @Override
  public Item item(final QueryContext qc, final InputInfo ii) {
    return toNode(Info.info(qc.context), SYSTEM);
  }
}
