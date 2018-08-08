package org.basex.query.func.zip;

import static org.basex.query.QueryError.ZIP_FAIL_X;
import static org.basex.util.Token.string;

import java.io.IOException;

import org.basex.core.MainOptions;
import org.basex.io.IO;
import org.basex.io.IOContent;
import org.basex.io.in.NewlineInput;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;

/**
 * Functions on zip files.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ZipTextEntry extends ZipBinaryEntry {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final String enc = exprs.length < 3 ? null : string(toToken(exprs[2], qc));
    final IO io = new IOContent(entry(qc));
    final boolean val = qc.context.options.get(MainOptions.CHECKSTRINGS);
    try {
      return Str.get(new NewlineInput(io).encoding(enc).validate(val).content());
    } catch(final IOException ex) {
      throw ZIP_FAIL_X.get(info, ex);
    }
  }
}
