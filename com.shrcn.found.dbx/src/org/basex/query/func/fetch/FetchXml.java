package org.basex.query.func.fetch;

import static org.basex.query.QueryError.BXFE_IO_X;
import static org.basex.query.QueryError.INVDOC_X;
import static org.basex.util.Token.string;

import java.io.IOException;

import org.basex.build.Parser;
import org.basex.core.MainOptions;
import org.basex.io.IO;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.up.primitives.DBOptions;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Uri;
import org.basex.query.value.node.DBNode;
import org.basex.util.InputInfo;
import org.basex.util.options.Options;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FetchXml extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] in = toToken(exprs[0], qc);
    final Options opts = toOptions(1, new Options(), qc);
    if(!Uri.uri(in).isValid()) throw INVDOC_X.get(info, in);

    final IO input = IO.get(string(in));
    final MainOptions mo = MainOptions.get();
    new DBOptions(opts, DBOptions.PARSING, info).assignTo(mo);

    try {
      return new DBNode(Parser.singleParser(input, mo, ""));
    } catch(final IOException ex) {
      throw BXFE_IO_X.get(info, ex);
    }
  }
}
