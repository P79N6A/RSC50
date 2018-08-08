package org.basex.query.expr;

import org.basex.query.CompileContext;
import org.basex.query.QueryException;
import org.basex.query.util.ASTVisitor;
import org.basex.query.value.node.FElem;
import org.basex.query.var.Var;
import org.basex.query.var.VarUsage;
import org.basex.util.InputInfo;

/**
 * Simple expression without arguments.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public abstract class Simple extends ParseExpr {
  /**
   * Constructor.
   * @param info input info
   */
  protected Simple(final InputInfo info) {
    super(info);
  }

  @Override
  public final void checkUp() {
  }

  @Override
  public Expr compile(final CompileContext cc) {
    return this;
  }

  @Override
  public boolean has(final Flag flag) {
    return false;
  }

  @Override
  public boolean removable(final Var var) {
    return true;
  }

  @Override
  public VarUsage count(final Var var) {
    return VarUsage.NEVER;
  }

  @Override
  public Expr inline(final Var var, final Expr ex, final CompileContext cc) throws QueryException {
    return null;
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem());
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return true;
  }

  @Override
  public int exprSize() {
    return 1;
  }
}
