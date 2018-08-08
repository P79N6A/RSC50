package org.basex.query.func.csv;

import static org.basex.query.QueryError.BXCS_PARSE_X;

import java.io.IOException;

import org.basex.build.csv.CsvParserOptions;
import org.basex.io.IOContent;
import org.basex.io.parse.csv.CsvConverter;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class CsvParse extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] input = toToken(exprs[0], qc);
    final CsvParserOptions opts = toOptions(1, new CsvParserOptions(), qc);
    try {
      return CsvConverter.get(opts).convert(new IOContent(input));
    } catch(final IOException ex) {
      throw BXCS_PARSE_X.get(info, ex);
    }
  }
}
