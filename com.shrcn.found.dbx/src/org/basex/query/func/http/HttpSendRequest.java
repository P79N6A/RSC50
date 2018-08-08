package org.basex.query.func.http;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.ANode;
import org.basex.util.http.HttpClient;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Rositsa Shadura
 */
public final class HttpSendRequest extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    checkCreate(qc);

    // get request node
    final ANode request = toEmptyNode(exprs[0], qc);

    // get HTTP URI
    final byte[] href = exprs.length >= 2 ? toEmptyToken(exprs[1], qc) : null;
    // get parameter $bodies
    Iter iter = null;
    if(exprs.length == 3) {
      final Iter bodies = exprs[2].iter(qc);
      final ValueBuilder cache = new ValueBuilder();
      for(Item body; (body = bodies.next()) != null;) cache.add(body);
      iter = cache.value().iter();
    }
    // send HTTP request
    return new HttpClient(info, qc.context.options).sendRequest(href, request, iter);
  }
}
