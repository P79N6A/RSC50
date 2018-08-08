package org.basex.query.func.fn;

import org.basex.core.locks.DBLocking;
import org.basex.data.Data;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.util.ASTVisitor;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Uri;
import org.basex.query.value.node.ANode;
import org.basex.query.value.type.NodeType;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnDocumentUri extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final ANode node = toEmptyNode(ctxArg(0, qc), qc);
    if(node == null || node.type != NodeType.DOC) return null;
    // return empty sequence for documents constructed via parse-xml
    final Data data = node.data();
    if(data != null && data.meta.name.isEmpty()) return null;

    final byte[] uri = node.baseURI();
    return uri.length == 0 ? null : Uri.uri(uri, false);
  }

  @Override
  public boolean has(final Flag flag) {
    return exprs.length == 0 && flag == Flag.CTX || super.has(flag);
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return (exprs.length != 0 || visitor.lock(DBLocking.CONTEXT)) && super.accept(visitor);
  }
}
