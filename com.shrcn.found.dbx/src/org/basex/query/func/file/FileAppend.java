package org.basex.query.func.file;

import java.io.IOException;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FileAppend extends FileWrite {
  @Override
  public Item item(final QueryContext qc) throws IOException, QueryException {
    return write(true, qc);
  }
}
