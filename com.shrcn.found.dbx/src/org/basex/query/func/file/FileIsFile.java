package org.basex.query.func.file;

import java.nio.file.Files;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Bln;
import org.basex.query.value.item.Item;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FileIsFile extends FileFn {
  @Override
  public Item item(final QueryContext qc) throws QueryException {
    return Bln.get(Files.isRegularFile(toPath(0, qc)));
  }
}
