package org.basex.query.func.file;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FileReadText extends FileRead {
  @Override
  public Item item(final QueryContext qc) throws QueryException {
    return text(qc);
  }
}
