package org.basex.query.func.file;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Uri;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FilePathToUri extends FileFn {
  @Override
  public Item item(final QueryContext qc) throws QueryException {
    return Uri.uri(toPath(0, qc).toUri().toString());
  }
}
