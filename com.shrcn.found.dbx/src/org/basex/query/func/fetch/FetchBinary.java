package org.basex.query.func.fetch;

import static org.basex.query.QueryError.BXFE_IO_X;

import org.basex.io.IO;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.B64Stream;
import org.basex.util.InputInfo;
import org.basex.util.Token;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FetchBinary extends StandardFunc {
  @Override
  public B64Stream item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] uri = toToken(exprs[0], qc);
    return new B64Stream(IO.get(Token.string(uri)), BXFE_IO_X);
  }
}
