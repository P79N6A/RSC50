package org.basex.query.func.archive;

import static org.basex.query.QueryError.ARCH_FAIL_X;
import static org.basex.util.Token.token;

import java.io.IOException;
import java.util.zip.ZipEntry;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.B64;
import org.basex.util.hash.TokenSet;
import org.basex.util.list.TokenList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class ArchiveExtractBinary extends ArchiveFn {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final ValueBuilder vb = new ValueBuilder();
    for(final byte[] b : extract(qc)) vb.add(new B64(b));
    return vb.value();
  }

  /**
   * Extracts entries from the archive.
   * @param qc query context
   * @return text entries
   * @throws QueryException query exception
   */
  final TokenList extract(final QueryContext qc) throws QueryException {
    final B64 archive = toB64(exprs[0], qc, false);
    final TokenSet hs = entries(1, qc);

    final TokenList tl = new TokenList();
    try(final ArchiveIn in = ArchiveIn.get(archive.input(info), info)) {
      while(in.more()) {
        final ZipEntry ze = in.entry();
        if(!ze.isDirectory() && (hs == null || hs.delete(token(ze.getName())) != 0))
          tl.add(in.read());
      }
    } catch(final IOException ex) {
      throw ARCH_FAIL_X.get(info, ex);
    }
    return tl;
  }
}
