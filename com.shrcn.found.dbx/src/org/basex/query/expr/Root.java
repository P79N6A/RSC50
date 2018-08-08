package org.basex.query.expr;

import static org.basex.query.QueryError.CTXNODE;

import org.basex.core.locks.DBLocking;
import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.BasicNodeIter;
import org.basex.query.iter.Iter;
import org.basex.query.util.ASTVisitor;
import org.basex.query.util.list.ANodeList;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.ANode;
import org.basex.query.value.type.NodeType;
import org.basex.query.value.type.SeqType;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * Root node.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Root extends Simple {
  /**
   * Constructor.
   * @param info input info
   */
  public Root(final InputInfo info) {
    super(info);
    seqType = SeqType.DOC_ZM;
  }

  @Override
  public Expr compile(final CompileContext cc) {
    return optimize(cc);
  }

  @Override
  public Expr optimize(final CompileContext cc) {
    final Value v = cc.qc.value;
    return v != null && v.type == NodeType.DOC && v.size() == 1 ? v : this;
  }

  @Override
  public BasicNodeIter iter(final QueryContext qc) throws QueryException {
    final Iter iter = ctxValue(qc).iter();
    final ANodeList list = new ANodeList().check();
    for(Item it; (it = iter.next()) != null;) {
      final ANode n = it instanceof ANode ? ((ANode) it).root() : null;
      if(n == null || n.type != NodeType.DOC) throw CTXNODE.get(info);
      list.add(n);
    }
    return list.iter();
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new Root(info);
  }

  @Override
  public boolean has(final Flag flag) {
    return flag == Flag.CTX;
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return visitor.lock(DBLocking.CONTEXT);
  }

  @Override
  public boolean iterable() {
    return true;
  }

  @Override
  public boolean sameAs(final Expr cmp) {
    return cmp instanceof Root;
  }

  @Override
  public String toString() {
    return "root()";
  }
}
