package org.basex.query.func.fn;

import static org.basex.query.QueryError.INVURI_X;

import org.basex.core.locks.DBLocking;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.util.ASTVisitor;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Uri;
import org.basex.query.value.node.ANode;
import org.basex.query.value.node.DBNode;
import org.basex.query.value.type.NodeType;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnBaseUri extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final ANode node = toEmptyNode(ctxArg(0, qc), qc);
    if(node == null) return null;
    if(node.type != NodeType.ELM && node.type != NodeType.DOC && node.parent() == null) return null;

    Uri base = Uri.EMPTY;
    ANode n = node;
    do {
      if(n == null) return sc.baseURI().resolve(base, info);
      final Uri bu = Uri.uri(n.baseURI(), false);
      if(!bu.isValid()) throw INVURI_X.get(info, n.baseURI());
      base = bu.resolve(base, info);
      if(n.type == NodeType.DOC && n instanceof DBNode) break;
      n = n.parent();
    } while(!base.isAbsolute());
    return base;
  }

  @Override
  public boolean has(final Flag flag) {
    return flag == Flag.CTX && exprs.length == 0 || super.has(flag);
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return (exprs.length != 0 || visitor.lock(DBLocking.CONTEXT)) && super.accept(visitor);
  }
}
