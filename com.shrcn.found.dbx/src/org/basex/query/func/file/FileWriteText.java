package org.basex.query.func.file;

import static org.basex.query.QueryError.FILE_UNKNOWN_ENCODING_X;
import static org.basex.util.Token.string;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

import org.basex.io.out.PrintOutput;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.util.Strings;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class FileWriteText extends FileFn {
  @Override
  public Item item(final QueryContext qc) throws IOException, QueryException {
    return write(false, qc);
  }

  /**
   * Writes items to a file.
   * @param append append flag
   * @param qc query context
   * @return true if file was successfully written
   * @throws QueryException query exception
   * @throws IOException I/O exception
   */
  final synchronized Item write(final boolean append, final QueryContext qc)
      throws QueryException, IOException {

    final Path path = checkParentDir(toPath(0, qc));
    final byte[] s = toToken(exprs[1], qc);
    final String enc = toEncoding(2, FILE_UNKNOWN_ENCODING_X, qc);
    final Charset cs = enc == null || enc == Strings.UTF8 ? null : Charset.forName(enc);

    try(final PrintOutput out = PrintOutput.get(new FileOutputStream(path.toFile(), append))) {
      out.write(cs == null ? s : string(s).getBytes(cs));
    }
    return null;
  }
}
