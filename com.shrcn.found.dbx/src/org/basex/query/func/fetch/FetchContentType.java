package org.basex.query.func.fetch;

import static org.basex.query.QueryError.BXFE_IO_X;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.basex.io.IO;
import org.basex.io.IOContent;
import org.basex.io.IOUrl;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;
import org.basex.util.Token;
import org.basex.util.http.MediaType;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FetchContentType extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] uri = toToken(exprs[0], qc);
    final IO io = IO.get(Token.string(uri));

    final String path = io.path();
    MediaType mt = null;
    if(io instanceof IOUrl) {
      try {
        final String ct = ((IOUrl) io).connection().getContentType();
        if(ct != null) mt = new MediaType(ct);
      } catch(final IOException ex) {
        throw BXFE_IO_X.get(info, ex);
      }
    } else if(io instanceof IOContent) {
      mt = MediaType.APPLICATION_XML;
    } else if(io.exists()) {
      mt = MediaType.get(path);
    }
    if(mt == null) throw BXFE_IO_X.get(info, new FileNotFoundException(path));
    return Str.get(mt.toString());
  }
}
