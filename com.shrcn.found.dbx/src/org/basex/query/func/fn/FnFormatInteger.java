package org.basex.query.func.fn;

import static org.basex.util.Token.EMPTY;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.util.format.Formatter;
import org.basex.query.util.format.IntFormat;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;
import org.basex.util.hash.TokenObjMap;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnFormatInteger extends StandardFunc {
  /** Pattern cache. */
  private final TokenObjMap<IntFormat> formats = new TokenObjMap<>();

  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] pic = toToken(exprs[1], qc);
    final byte[] lng = exprs.length == 2 ? EMPTY : toToken(exprs[2], qc);

    final Item it = exprs[0].atomItem(qc, info);
    if(it == null) return Str.ZERO;
    final long num = toLong(it);

    IntFormat format;
    synchronized(formats) {
      format = formats.get(pic);
      if(format == null) {
        format = new IntFormat(pic, info);
        formats.put(pic, format);
      }
    }
    return Str.get(Formatter.get(lng).formatInt(num, format));
  }
}
