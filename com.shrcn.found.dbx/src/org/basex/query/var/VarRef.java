package org.basex.query.var;

import static org.basex.query.QueryText.DOLLAR;
import static org.basex.query.QueryText.VARBL;

import org.basex.data.Data;
import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.expr.ParseExpr;
import org.basex.query.iter.Iter;
import org.basex.query.util.ASTVisitor;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.FElem;
import org.basex.util.InputInfo;
import org.basex.util.TokenBuilder;
import org.basex.util.hash.IntObjMap;

/**
 * Local Variable Reference expression.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 * @author Leo Woerteler
 */
public final class VarRef extends ParseExpr {
  /** Variable name. */
  public final Var var;

  /**
   * Constructor.
   * @param info input info
   * @param var variable
   */
  public VarRef(final InputInfo info, final Var var) {
    super(info);
    this.var = var;
  }

  @Override
  public Expr compile(final CompileContext cc) {
    return optimize(cc);
  }

  @Override
  public VarRef optimize(final CompileContext cc) {
    seqType = var.seqType();
    size = var.size;
    return this;
  }

  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return qc.get(var).item(qc, info);
  }

  @Override
  public Iter iter(final QueryContext qc) {
    return qc.get(var).iter();
  }

  @Override
  public Value value(final QueryContext qc) {
    return qc.get(var);
  }

  @Override
  public Data data() {
    return var.data;
  }

  @Override
  public boolean removable(final Var v) {
    return true;
  }

  @Override
  public VarUsage count(final Var v) {
    return var.is(v) ? VarUsage.ONCE : VarUsage.NEVER;
  }

  @Override
  public Expr inline(final Var v, final Expr ex, final CompileContext cc) {
    // [LW] Is copying always necessary?
    return var.is(v) ? ex.isValue() ? ex : ex.copy(cc, new IntObjMap<Var>()) : null;
  }

  @Override
  public VarRef copy(final CompileContext cc, final IntObjMap<Var> vm) {
    final Var nw = vm.get(var.id);
    return new VarRef(info, nw != null ? nw : var).optimize(cc);
  }

  @Override
  public boolean sameAs(final Expr cmp) {
    return cmp instanceof VarRef && var.is(((VarRef) cmp).var);
  }

  @Override
  public void plan(final FElem plan) {
    final FElem e = planElem();
    var.plan(e);
    plan.add(e);
  }

  @Override
  public String description() {
    return VARBL;
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return visitor.used(this);
  }

  @Override
  public void checkUp() {
  }

  @Override
  public String toErrorString() {
    return new TokenBuilder(DOLLAR).addExt(var.name.string()).toString();
  }

  @Override
  public String toString() {
    return new TokenBuilder(DOLLAR).addExt(var.name.string()).add('_').addInt(var.id).toString();
  }

  @Override
  public boolean has(final Flag flag) {
    return false;
  }

  @Override
  public int exprSize() {
    return 1;
  }
}
