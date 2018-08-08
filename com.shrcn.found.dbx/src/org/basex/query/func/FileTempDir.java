package org.basex.query.func;

import org.basex.query.QueryContext;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;
import org.basex.util.Prop;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FileTempDir extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) {
    return Str.get(Prop.TMP);
  }
}
