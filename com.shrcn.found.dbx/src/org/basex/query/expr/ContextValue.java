package org.basex.query.expr;

import org.basex.core.locks.DBLocking;
import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.util.ASTVisitor;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.query.value.type.SeqType;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * Context value.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ContextValue extends Simple {
  /**
   * Constructor.
   * @param info input info
   */
  public ContextValue(final InputInfo info) {
    super(info);
    seqType = SeqType.ITEM_ZM;
  }

  @Override
  public ContextValue compile(final CompileContext cc) {
    return optimize(cc);
  }

  @Override
  public ContextValue optimize(final CompileContext cc) {
    if(cc.qc.value != null) seqType = cc.qc.value.seqType();
    return this;
  }

  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return ctxValue(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    return ctxValue(qc);
  }

  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return ctxValue(qc).item(qc, info);
  }

  @Override
  public boolean has(final Flag flag) {
    return flag == Flag.CTX;
  }

  @Override
  public boolean removable(final Var var) {
    return false;
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return copyType(new ContextValue(info));
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return visitor.lock(DBLocking.CONTEXT) && super.accept(visitor);
  }

  @Override
  public boolean sameAs(final Expr cmp) {
    return cmp instanceof ContextValue;
  }

  @Override
  public String toString() {
    return ".";
  }
}
