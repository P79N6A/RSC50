package org.basex.query.func.file;

import java.nio.file.Paths;

import org.basex.query.QueryContext;
import org.basex.query.value.item.Item;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FileCurrentDir extends FileFn {
  @Override
  public Item item(final QueryContext qc) {
    return get(absolute(Paths.get(".")), true);
  }
}
