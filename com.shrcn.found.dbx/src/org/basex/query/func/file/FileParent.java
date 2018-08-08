package org.basex.query.func.file;

import java.nio.file.Path;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FileParent extends FileFn {
  @Override
  public Item item(final QueryContext qc) throws QueryException {
    final Path parent = absolute(toPath(0, qc)).getParent();
    return parent == null ? null : get(parent, true);
  }
}
