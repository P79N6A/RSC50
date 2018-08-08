package org.basex.query.func.xslt;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;

/**
 * Functions for performing XSLT transformations.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class XsltTransformText extends XsltTransform {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return Str.get(transform(qc));
  }
}
