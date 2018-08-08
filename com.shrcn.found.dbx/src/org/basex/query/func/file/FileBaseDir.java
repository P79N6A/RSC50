package org.basex.query.func.file;

import java.nio.file.Paths;

import org.basex.io.IO;
import org.basex.io.IOFile;
import org.basex.query.QueryContext;
import org.basex.query.value.item.Item;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FileBaseDir extends FileFn {
  @Override
  public Item item(final QueryContext qc) {
    final IO base = sc.baseIO();
    if(!(base instanceof IOFile)) return null;
    return get(absolute(Paths.get(base.isDir() ? base.path() : base.dir())), true);
  }
}
