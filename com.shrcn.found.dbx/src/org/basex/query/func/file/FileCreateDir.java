package org.basex.query.func.file;

import static org.basex.query.QueryError.FILE_EXISTS_X;

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
public final class FileCreateDir extends FileFn {
  @Override
  public Item item(final QueryContext qc) throws QueryException, IOException {
    final Path path = absolute(toPath(0, qc));

    // find lowest existing path
    for(Path p = path; p != null;) {
      if(Files.exists(p)) {
        if(Files.isRegularFile(p)) throw FILE_EXISTS_X.get(info, p);
        break;
      }
      p = p.getParent();
    }

    Files.createDirectories(path);
    return null;
  }
}
