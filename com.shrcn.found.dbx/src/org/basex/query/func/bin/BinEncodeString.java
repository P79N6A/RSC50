package org.basex.query.func.bin;

import static org.basex.query.QueryError.BIN_CE_X;
import static org.basex.query.QueryError.BIN_UE_X;

import java.nio.charset.CharacterCodingException;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.convert.ConvertFn;
import org.basex.query.value.item.B64;
import org.basex.util.InputInfo;
import org.basex.util.Strings;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class BinEncodeString extends BinFn {
  @Override
  public B64 item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] str = str(0, qc);
    final String enc = toEncoding(1, BIN_UE_X, qc);
    if(str == null) return null;
    try {
      return new B64(enc == null || enc == Strings.UTF8 ? str : ConvertFn.toBinary(str, enc));
    } catch(final CharacterCodingException ex) {
      throw BIN_CE_X.get(info, ex);
    }
  }
}
