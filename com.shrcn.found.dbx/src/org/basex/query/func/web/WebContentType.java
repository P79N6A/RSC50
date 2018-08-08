package org.basex.query.func.web;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
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
public final class WebContentType extends StandardFunc {
  @Override
  public Str item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return Str.get(MediaType.get(Token.string(toToken(exprs[0], qc))).toString());
  }
}
