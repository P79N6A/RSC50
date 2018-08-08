package org.basex.query.func.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;
import org.basex.util.Strings;
import org.basex.util.Token;
import org.basex.util.Util;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class WebEncodeUrl extends WebFn {
  @Override
  public Str item(final QueryContext qc, final InputInfo ii) throws QueryException {
    try {
      return Str.get(URLEncoder.encode(Token.string(toToken(exprs[0], qc)), Strings.UTF8));
    } catch(final UnsupportedEncodingException ex) {
      /* UTF8 is always supported */
      throw Util.notExpected();
    }
  }
}
