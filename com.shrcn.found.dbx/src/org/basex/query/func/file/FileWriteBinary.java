package org.basex.query.func.file;

import static org.basex.query.QueryError.FILE_OUT_OF_RANGE_X_X;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

import org.basex.io.in.BufferInput;
import org.basex.io.out.BufferOutput;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Bin;
import org.basex.query.value.item.Item;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class FileWriteBinary extends FileFn {
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
    final Bin bin = toBin(exprs[1], qc);

    // write full file
    if(exprs.length == 2) {
      try(final BufferOutput out = new BufferOutput(new FileOutputStream(path.toFile(), append));
          final BufferInput in = bin.input(info)) {
        for(int b; (b = in.read()) != -1;) out.write(b);
      }
    } else {
      // write file chunk
      final long off = toLong(exprs[2], qc);
      try(final RandomAccessFile raf = new RandomAccessFile(path.toFile(), "rw")) {
        final long dlen = raf.length();
        if(off < 0 || off > dlen) throw FILE_OUT_OF_RANGE_X_X.get(info, off, dlen);
        raf.seek(off);
        raf.write(bin.binary(info));
      }
    }
    return null;
  }
}
