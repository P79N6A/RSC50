package org.basex.query.func.zip;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Functions on zip files.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ZipHtmlEntry extends ZipXmlEntry {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return xmlEntry(qc, true);
  }
}
