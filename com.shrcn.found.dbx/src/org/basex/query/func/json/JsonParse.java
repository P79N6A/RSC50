package org.basex.query.func.json;

import org.basex.build.json.JsonParserOptions;
import org.basex.io.parse.json.JsonConverter;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryIOException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class JsonParse extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] input = toToken(exprs[0], qc);
    final JsonParserOptions opts = toOptions(1, new JsonParserOptions(), qc);
    try {
      return JsonConverter.get(opts).convert(input, null);
    } catch(final QueryIOException ex) {
      throw ex.getCause(info);
    }
  }
}
