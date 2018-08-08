package org.basex.query.func.file;

import static org.basex.query.QueryError.FILE_IO_ERROR_X;
import static org.basex.query.QueryError.FILE_IS_DIR_X;
import static org.basex.query.QueryError.FILE_NOT_FOUND_X;
import static org.basex.query.QueryError.FILE_OUT_OF_RANGE_X_X;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.basex.io.IOFile;
import org.basex.io.random.DataAccess;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.B64;
import org.basex.query.value.item.B64Stream;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FileReadBinary extends FileFn {
  @Override
  public B64 item(final QueryContext qc) throws QueryException, IOException {
    final Path path = toPath(0, qc);
    final long off = exprs.length > 1 ? toLong(exprs[1], qc) : 0;
    long len = exprs.length > 2 ? toLong(exprs[2], qc) : 0;

    if(!Files.exists(path)) throw FILE_NOT_FOUND_X.get(info, path);
    if(Files.isDirectory(path)) throw FILE_IS_DIR_X.get(info, path);

    // read full file
    if(exprs.length == 1) return new B64Stream(new IOFile(path.toFile()), FILE_IO_ERROR_X);

    // read file chunk
    try(final DataAccess da = new DataAccess(new IOFile(path.toFile()))) {
      final long dlen = da.length();
      if(exprs.length == 2) len = dlen - off;
      if(off < 0 || off > dlen || len < 0 || off + len > dlen)
        throw FILE_OUT_OF_RANGE_X_X.get(info, off, off + len);
      da.cursor(off);
      return new B64(da.readBytes((int) len));
    }
  }
}
