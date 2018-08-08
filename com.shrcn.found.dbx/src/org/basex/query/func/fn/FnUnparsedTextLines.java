package org.basex.query.func.fn;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.query.value.seq.Empty;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnUnparsedTextLines extends Parse {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final Item it = unparsedText(qc, false, true);
    return it == null ? Empty.ITER : textIter(it.string(info));
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    return iter(qc).value();
  }

  @Override
  public boolean iterable() {
    // collections will never yield duplicates
    return true;
  }
}
