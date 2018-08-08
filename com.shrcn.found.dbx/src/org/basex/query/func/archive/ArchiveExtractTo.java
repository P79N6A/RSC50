package org.basex.query.func.archive;

import static org.basex.query.QueryError.ARCH_FAIL_X;
import static org.basex.util.Token.token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.B64;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.hash.TokenSet;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class ArchiveExtractTo extends ArchiveFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    checkCreate(qc);

    final Path path = toPath(0, qc);
    final B64 archive = toB64(exprs[1], qc, false);
    final TokenSet hs = entries(2, qc);

    try(final ArchiveIn in = ArchiveIn.get(archive.input(info), info)) {
      while(in.more()) {
        final ZipEntry ze = in.entry();
        final String name = ze.getName();
        if(hs == null || hs.delete(token(name)) != 0) {
          final Path file = path.resolve(name);
          if(ze.isDirectory()) {
            Files.createDirectories(file);
          } else {
            Files.createDirectories(file.getParent());
            Files.write(file, in.read());
          }
        }
      }
    } catch(final IOException ex) {
      throw ARCH_FAIL_X.get(info, ex);
    }
    return null;
  }
}
