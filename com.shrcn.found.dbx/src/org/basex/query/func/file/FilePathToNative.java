package org.basex.query.func.file;

import java.io.IOException;
import java.nio.file.Files;
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
public final class FilePathToNative extends FileFn {
  @Override
  public Item item(final QueryContext qc) throws QueryException, IOException {
    final Path nat = toPath(0, qc).toRealPath();
    return get(nat, Files.isDirectory(nat));
  }
}
