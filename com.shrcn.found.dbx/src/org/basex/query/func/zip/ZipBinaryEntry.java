package org.basex.query.func.zip;

import static org.basex.query.QueryError.ZIP_FAIL_X;
import static org.basex.query.QueryError.ZIP_NOTFOUND_X;
import static org.basex.util.Token.string;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.basex.io.IOFile;
import org.basex.io.Zip;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.B64;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Functions on zip files.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class ZipBinaryEntry extends ZipFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return new B64(entry(qc));
  }

  /**
   * Returns an entry from a zip file.
   * @param qc query context
   * @return binary result
   * @throws QueryException query exception
   */
  final byte[] entry(final QueryContext qc) throws QueryException {
    checkCreate(qc);
    final IOFile file = new IOFile(string(toToken(exprs[0], qc)));
    final String path = string(toToken(exprs[1], qc));
    if(!file.exists()) throw ZIP_NOTFOUND_X.get(info, file);

    try {
      return new Zip(file).read(path);
    } catch(final FileNotFoundException ex) {
      throw ZIP_NOTFOUND_X.get(info, file + "/" + path);
    } catch(final IOException ex) {
      throw ZIP_FAIL_X.get(info, ex);
    }
  }
}
