package org.basex.query.func.fn;

import org.basex.query.QueryContext;
import org.basex.query.QueryText;
import org.basex.query.func.StandardFunc;
import org.basex.query.util.collation.Collation;
import org.basex.query.value.item.Uri;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnDefaultCollation extends StandardFunc {
  @Override
  public Uri item(final QueryContext qc, final InputInfo ii) {
    final Collation coll = sc.collation;
    return Uri.uri(coll == null ? QueryText.COLLATION_URI : coll.uri());
  }
}
