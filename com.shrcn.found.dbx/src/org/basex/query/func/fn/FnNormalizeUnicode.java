package org.basex.query.func.fn;

import static org.basex.query.QueryError.NORMUNI_X;
import static org.basex.util.Token.ascii;
import static org.basex.util.Token.string;
import static org.basex.util.Token.trim;
import static org.basex.util.Token.uc;

import java.text.Normalizer;
import java.text.Normalizer.Form;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnNormalizeUnicode extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] str = toEmptyToken(exprs[0], qc);

    Form form = Form.NFC;
    if(exprs.length == 2) {
      final byte[] n = uc(trim(toToken(exprs[1], qc)));
      if(n.length == 0) return Str.get(str);
      try {
        form = Form.valueOf(string(n));
      } catch(final IllegalArgumentException ex) {
        throw NORMUNI_X.get(info, n);
      }
    }
    return ascii(str) ? Str.get(str) : Str.get(Normalizer.normalize(string(str), form));
  }
}
