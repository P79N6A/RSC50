package org.basex.query.func.file;

import java.io.IOException;
import java.nio.file.DirectoryStream;
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
public final class FileDelete extends FileFn {
  @Override
  public Item item(final QueryContext qc) throws QueryException, IOException {
    final Path path = toPath(0, qc);
    if(optionalBool(1, qc)) {
      delete(path);
    } else {
      Files.delete(path);
    }
    return null;
  }

  /**
   * Recursively deletes a file path.
   * @param path path to be deleted
   * @throws IOException I/O exception
   */
  private synchronized void delete(final Path path) throws IOException {
    if(Files.isDirectory(path)) {
      try(DirectoryStream<Path> paths = Files.newDirectoryStream(path)) {
        for(final Path p : paths) delete(p);
      }
    }
    Files.delete(path);
  }
}
