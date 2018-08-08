package org.basex.query.func.file;

import static org.basex.query.QueryError.FILE_IO_ERROR_X;
import static org.basex.query.QueryError.FILE_IS_DIR_X;
import static org.basex.query.QueryError.FILE_NOT_FOUND_X;
import static org.basex.query.QueryError.FILE_UNKNOWN_ENCODING_X;

import java.nio.file.Files;
import java.nio.file.Path;

import org.basex.io.IOFile;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.StrStream;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
abstract class FileRead extends FileFn {
  /**
   * Reads the contents of a file.
   * @param qc query context
   * @return string
   * @throws QueryException query exception
   */
  final StrStream text(final QueryContext qc) throws QueryException {
    final Path path = toPath(0, qc);
    final String enc = toEncoding(1, FILE_UNKNOWN_ENCODING_X, qc);
    final boolean val = exprs.length < 3 || !toBoolean(exprs[2], qc);
    if(!Files.exists(path)) throw FILE_NOT_FOUND_X.get(info, path);
    if(Files.isDirectory(path)) throw FILE_IS_DIR_X.get(info, path);
    return new StrStream(new IOFile(path.toFile()), enc, FILE_IO_ERROR_X, val);
  }
}
