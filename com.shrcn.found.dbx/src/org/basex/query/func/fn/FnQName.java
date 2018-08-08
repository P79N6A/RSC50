package org.basex.query.func.fn;

import static org.basex.query.QueryError.valueError;
import static org.basex.query.QueryText.XML_URI;
import static org.basex.util.Token.XMLC;
import static org.basex.util.Token.concat;
import static org.basex.util.Token.contains;
import static org.basex.util.Token.eq;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.QNm;
import org.basex.query.value.type.AtomType;
import org.basex.util.InputInfo;
import org.basex.util.XMLToken;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnQName extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] uri = toEmptyToken(exprs[0], qc);
    final byte[] name = toToken(exprs[1], qc);
    final byte[] str = !contains(name, ':') && eq(uri, XML_URI) ? concat(XMLC, name) : name;
    if(!XMLToken.isQName(str)) throw valueError(AtomType.QNM, name, info);
    final QNm nm = new QNm(str, uri);
    if(nm.hasPrefix() && uri.length == 0) throw valueError(AtomType.URI, nm.uri(), info);
    return nm;
  }
}
