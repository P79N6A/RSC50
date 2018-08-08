package org.basex.query.func.crypto;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Lukas Kircher
 */
public final class CryptoEncrypt extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return new Encryption(info).encryption(toToken(exprs[0], qc),
        toToken(exprs[1], qc), toToken(exprs[2], qc), toToken(exprs[3], qc), true);
  }
}
