package org.basex.query.func.web;

import static org.basex.query.QueryText.HTTP_PREFIX;
import static org.basex.query.QueryText.HTTP_URI;
import static org.basex.query.QueryText.REST_PREFIX;
import static org.basex.query.QueryText.REST_URI;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.QNm;
import org.basex.query.value.map.Map;
import org.basex.query.value.node.FElem;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class WebRedirect extends WebFn {
  @Override
  public FElem item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] url = createUrl(toToken(exprs[0], qc),
        exprs.length < 2 ? Map.EMPTY : toMap(exprs[1], qc));

    final FElem hhead = new FElem(new QNm(HTTP_PREFIX, "header", HTTP_URI)).
        add("name", "location").add("value", url);
    final FElem hresp = new FElem(new QNm(HTTP_PREFIX, "response", HTTP_URI)).
        add("status", "302").add(hhead);
    return new FElem(new QNm(REST_PREFIX, "response", REST_URI)).add(hresp);
  }
}
