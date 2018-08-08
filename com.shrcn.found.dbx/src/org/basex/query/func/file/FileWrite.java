package org.basex.query.func.file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import org.basex.io.out.PrintOutput;
import org.basex.io.serial.Serializer;
import org.basex.io.serial.SerializerOptions;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryIOException;
import org.basex.query.func.FuncOptions;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class FileWrite extends FileFn {
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
    final Value value = qc.value(exprs[1]);
    final Item so = exprs.length > 2 ? exprs[2].item(qc, info) : null;
    final SerializerOptions sopts = FuncOptions.serializer(so, info);

    try(final PrintOutput out = PrintOutput.get(new FileOutputStream(path.toFile(), append))) {
      try(final Serializer ser = Serializer.get(out, sopts)) {
        for(final Item it : value) ser.serialize(it);
      }
    } catch(final QueryIOException ex) {
      throw ex.getCause(info);
    }
    return null;
  }

}
