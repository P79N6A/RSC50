package org.basex.query.func.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FileSize extends FileFn {
  @Override
  public Item item(final QueryContext qc) throws IOException, QueryException {
    final Path path = toPath(0, qc);
    final BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
    return Int.get(attrs.isDirectory() ? 0 : attrs.size());
  }
}
