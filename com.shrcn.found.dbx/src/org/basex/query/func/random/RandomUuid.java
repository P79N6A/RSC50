package org.basex.query.func.random;

import java.util.UUID;

import org.basex.query.QueryContext;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Dirk Kirsten
 */
public final class RandomUuid extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) {
    return Str.get(UUID.randomUUID().toString());
  }
}
