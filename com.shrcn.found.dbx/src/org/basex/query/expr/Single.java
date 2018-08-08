package org.basex.query.expr;

import org.basex.query.CompileContext;
import org.basex.query.QueryException;
import org.basex.query.util.ASTVisitor;
import org.basex.query.value.node.FElem;
import org.basex.query.var.Var;
import org.basex.query.var.VarUsage;
import org.basex.util.InputInfo;

/**
 * Abstract single expression.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public abstract class Single extends ParseExpr {
  /** Expression. */
  public Expr expr;

  /**
   * Constructor.
   * @param info input info
   * @param expr expression
   */
  protected Single(final InputInfo info, final Expr expr) {
    super(info);
    this.expr = expr;
  }

  @Override
  public void checkUp() throws QueryException {
    checkNoUp(expr);
  }

  @Override
  public Expr compile(final CompileContext cc) throws QueryException {
    expr = expr.compile(cc);
    return this;
  }

  @Override
  public boolean has(final Flag flag) {
    return expr.has(flag);
  }

  @Override
  public boolean removable(final Var var) {
    return expr.removable(var);
  }

  @Override
  public VarUsage count(final Var var) {
    return expr.count(var);
  }

  @Override
  public Expr inline(final Var var, final Expr ex, final CompileContext cc) throws QueryException {
    final Expr sub = expr.inline(var, ex, cc);
    if(sub == null) return null;
    expr = sub;
    return optimize(cc);
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(), expr);
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return expr.accept(visitor);
  }

  @Override
  public int exprSize() {
    return expr.exprSize() + 1;
  }
}
