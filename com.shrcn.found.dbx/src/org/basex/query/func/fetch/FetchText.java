package org.basex.query.func.fetch;

import static org.basex.query.QueryError.BXFE_ENCODING_X;
import static org.basex.query.QueryError.BXFE_IO_X;

import org.basex.io.IO;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.StrStream;
import org.basex.util.InputInfo;
import org.basex.util.Token;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FetchText extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] uri = toToken(exprs[0], qc);
    final String enc = toEncoding(1, BXFE_ENCODING_X, qc);
    final boolean val = exprs.length < 3 || !toBoolean(exprs[2], qc);
    return new StrStream(IO.get(Token.string(uri)), enc, BXFE_IO_X, val);
  }
}
