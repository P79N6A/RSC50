package org.basex.query.func.file;

import static org.basex.query.QueryError.FILE_NO_DIR_X;
import static org.basex.util.Token.string;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.util.Prop;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class FileCreateTempFile extends FileFn {
  @Override
  public Item item(final QueryContext qc) throws QueryException, IOException {
    return createTemp(false, qc);
  }

  /**
   * Creates a temporary file or directory.
   * @param qc query context
   * @param dir create a directory instead of a file
   * @return path of created file or directory
   * @throws QueryException query exception
   * @throws IOException I/O exception
   */
  final synchronized Item createTemp(final boolean dir, final QueryContext qc)
      throws QueryException, IOException {

    final String pref = string(toToken(exprs[0], qc));
    final String suf = exprs.length > 1 ? string(toToken(exprs[1], qc)) : "";
    final Path root;
    if(exprs.length > 2) {
      root = toPath(2, qc);
      if(Files.isRegularFile(root)) throw FILE_NO_DIR_X.get(info, root);
    } else {
      root = Paths.get(Prop.TMP);
    }

    // choose non-existing file path
    final Random rnd = new Random();
    Path file;
    do {
      file = root.resolve(pref + rnd.nextLong() + suf);
    } while(Files.exists(file));

    // create directory or file
    if(dir) {
      Files.createDirectory(file);
    } else {
      Files.createFile(file);
    }
    return get(file, dir);
  }
}
