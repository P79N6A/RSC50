package org.basex.query.func.fn;

import static org.basex.query.QueryText.XML_URI;
import static org.basex.util.Token.XML;
import static org.basex.util.Token.eq;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Uri;
import org.basex.query.value.node.ANode;
import org.basex.util.Atts;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnNamespaceUriForPrefix extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] pref = toEmptyToken(exprs[0], qc);
    final ANode an = toElem(exprs[1], qc);
    if(eq(pref, XML)) return Uri.uri(XML_URI, false);
    final Atts at = an.nsScope(sc);
    final byte[] s = at.value(pref);
    return s == null || s.length == 0 ? null : Uri.uri(s, false);
  }
}
