package org.basex.query.func.archive;

import static org.basex.query.QueryError.ARCH_ENCODING_X;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.Str;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ArchiveExtractText extends ArchiveExtractBinary {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final String enc = toEncoding(2, ARCH_ENCODING_X, qc);
    final ValueBuilder vb = new ValueBuilder();
    for(final byte[] b : extract(qc)) vb.add(Str.get(encode(b, enc, qc)));
    return vb.value();
  }
}
