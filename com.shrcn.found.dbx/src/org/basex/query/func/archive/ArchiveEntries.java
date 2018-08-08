package org.basex.query.func.archive;

import static org.basex.query.QueryError.ARCH_FAIL_X;
import static org.basex.query.func.archive.ArchiveText.COMP_SIZE;
import static org.basex.query.func.archive.ArchiveText.LAST_MOD;
import static org.basex.query.func.archive.ArchiveText.Q_ENTRY;
import static org.basex.query.func.archive.ArchiveText.SIZE;
import static org.basex.util.Token.token;

import java.io.IOException;
import java.util.zip.ZipEntry;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.B64;
import org.basex.query.value.item.Dtm;
import org.basex.query.value.node.FElem;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ArchiveEntries extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final B64 archive = toB64(exprs[0], qc, false);
    final ValueBuilder vb = new ValueBuilder();
    try(final ArchiveIn in = ArchiveIn.get(archive.input(info), info)) {
      while(in.more()) {
        final ZipEntry ze = in.entry();
        if(ze.isDirectory()) continue;
        final FElem e = new FElem(Q_ENTRY).declareNS();
        long s = ze.getSize();
        if(s != -1) e.add(SIZE, token(s));
        s = ze.getTime();
        if(s != -1) e.add(LAST_MOD, Dtm.get(s).string(info));
        s = ze.getCompressedSize();
        if(s != -1) e.add(COMP_SIZE, token(s));
        e.add(ze.getName());
        vb.add(e);
      }
      return vb.value();
    } catch(final IOException ex) {
      throw ARCH_FAIL_X.get(info, ex);
    }
  }
}
