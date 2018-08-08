package org.basex.query.func.file;

import java.nio.file.Path;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FileName extends FileFn {
  @Override
  public Item item(final QueryContext qc) throws QueryException {
    final Path path = toPath(0, qc).getFileName();
    return path == null ? Str.ZERO : Str.get(path.toString());
  }
}
