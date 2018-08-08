package org.basex.query.func.fn;

import static org.basex.query.QueryError.BXJS_DUPLICATE_X;
import static org.basex.query.QueryError.BXJS_INVALID_X;
import static org.basex.query.QueryError.BXJS_PARSE_X_X_X;
import static org.basex.query.QueryError.JSON_DUPLICATE_X;
import static org.basex.query.QueryError.JSON_OPT_X;
import static org.basex.query.QueryError.JSON_PARSE_X;

import org.basex.build.json.JsonOptions;
import org.basex.build.json.JsonOptions.JsonFormat;
import org.basex.build.json.JsonParserOptions;
import org.basex.io.parse.json.JsonConverter;
import org.basex.io.parse.json.JsonFallback;
import org.basex.query.QueryContext;
import org.basex.query.QueryError;
import org.basex.query.QueryException;
import org.basex.query.QueryIOException;
import org.basex.query.QueryRTException;
import org.basex.query.func.FuncOptions;
import org.basex.query.value.item.FItem;
import org.basex.query.value.item.FuncItem;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.query.value.type.FuncType;
import org.basex.query.value.type.SeqType;
import org.basex.util.InputInfo;
import org.basex.util.Token;
import org.basex.util.Util;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class FnParseJson extends Parse {
  /** Function taking and returning a string. */
  private static final FuncType STRFUNC = FuncType.get(SeqType.STR, SeqType.STR);

  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Item it = exprs[0].atomItem(qc, info);
    if(it == null) return null;
    return parse(toToken(it), false, qc, info);
  }

  /**
   * Parses the specified JSON string.
   * @param json json string
   * @param xml convert to xml
   * @param qc query context
   * @param ii input info
   * @return resulting item
   * @throws QueryException query exception
   */
  final Item parse(final byte[] json, final boolean xml, final QueryContext qc,
      final InputInfo ii) throws QueryException {

    final JsonParserOptions opts = new JsonParserOptions();
    if(exprs.length > 1) new FuncOptions(info).acceptUnknown().assign(toMap(exprs[1], qc), opts);

    final boolean esc = opts.get(JsonParserOptions.ESCAPE);
    final FuncItem fb = opts.get(JsonParserOptions.FALLBACK);
    final FItem fallback;
    if(fb == null) {
      fallback = null;
    } else {
      fallback = STRFUNC.cast(fb, qc, sc, ii);
    }
    if(esc && fallback != null) throw JSON_OPT_X.get(ii,
        "Escaping cannot be combined with a fallback function.");

    try {
      opts.set(JsonOptions.FORMAT, xml ? JsonFormat.BASIC : JsonFormat.MAP);
      final JsonConverter conv = JsonConverter.get(opts);
      if(!esc && fallback != null) conv.fallback(new JsonFallback() {
        @Override
        public String convert(final String string) {
          try {
            return Token.string(fallback.invokeItem(qc, ii, Str.get(string)).string(ii));
          } catch(final QueryException ex) {
            throw new QueryRTException(ex);
          }
        }
      });
      return conv.convert(json, null);
    } catch(final QueryRTException ex) {
      throw ex.getCause();
    } catch(final QueryIOException ex) {
      Util.debug(ex);
      final QueryException qe = ex.getCause(info);
      final QueryError error = qe.error();
      final String message = ex.getLocalizedMessage();
      if(error == BXJS_PARSE_X_X_X) throw JSON_PARSE_X.get(ii, message);
      if(error == BXJS_DUPLICATE_X) throw JSON_DUPLICATE_X.get(ii, message);
      if(error == BXJS_INVALID_X) throw JSON_OPT_X.get(ii, message);
      throw qe;
    }
  }
}
