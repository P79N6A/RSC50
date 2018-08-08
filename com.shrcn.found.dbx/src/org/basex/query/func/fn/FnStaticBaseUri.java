package org.basex.query.func.fn;

import org.basex.query.QueryContext;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Uri;
import org.basex.util.InputInfo;

/**
 * Function implementation.).
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnStaticBaseUri extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) {
    final Uri uri = sc.baseURI();
    return uri == Uri.EMPTY ? null : uri;
  }
}
