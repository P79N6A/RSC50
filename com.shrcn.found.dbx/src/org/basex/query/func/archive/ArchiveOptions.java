package org.basex.query.func.archive;

import static org.basex.query.QueryError.ARCH_FAIL_X;
import static org.basex.query.func.archive.ArchiveText.DEFLATE;
import static org.basex.query.func.archive.ArchiveText.STORED;
import static org.basex.query.func.archive.ArchiveText.UNKNOWN;

import java.io.IOException;
import java.util.zip.ZipEntry;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.B64;
import org.basex.query.value.item.Str;
import org.basex.query.value.map.Map;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ArchiveOptions extends StandardFunc {
  @Override
  public Map item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final B64 archive = toB64(exprs[0], qc, false);
    final String format;
    int level = -1;

    try(final ArchiveIn arch = ArchiveIn.get(archive.input(info), info)) {
      format = arch.format();
      while(arch.more()) {
        final ZipEntry ze = arch.entry();
        if(ze.isDirectory()) continue;
        level = ze.getMethod();
        break;
      }
    } catch(final IOException ex) {
      throw ARCH_FAIL_X.get(info, ex);
    }

    // create result element
    Map map = Map.EMPTY;
    if(format != null) {
      map = map.put(Str.get(ArchOptions.FORMAT.name()), Str.get(format), ii);
    }
    if(level >= 0) {
      final String lvl = level == 8 ? DEFLATE : level == 0 ? STORED : UNKNOWN;
      map = map.put(Str.get(ArchOptions.ALGORITHM.name()), Str.get(lvl), ii);
    }
    return map;
  }
}
