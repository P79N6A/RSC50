package org.basex.query.expr.ft;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.expr.ParseExpr;
import org.basex.query.iter.FTIter;
import org.basex.query.util.ASTVisitor;
import org.basex.query.value.node.FElem;
import org.basex.query.value.node.FTNode;
import org.basex.query.value.type.SeqType;
import org.basex.query.var.Var;
import org.basex.query.var.VarUsage;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * This class defines is an abstract class for full-text expressions.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public abstract class FTExpr extends ParseExpr {
  /** Expressions. */
  public final FTExpr[] exprs;

  /**
   * Constructor.
   * @param info input info
   * @param exprs expressions
   */
  FTExpr(final InputInfo info, final FTExpr... exprs) {
    super(info);
    this.exprs = exprs;
    seqType = SeqType.BLN;
  }

  @Override
  public void checkUp() throws QueryException {
    checkNoneUp(exprs);
  }

  @Override
  public FTExpr compile(final CompileContext cc) throws QueryException {
    final int es = exprs.length;
    for(int e = 0; e < es; e++) exprs[e] = exprs[e].compile(cc);
    return this;
  }

  @Override
  public FTExpr optimize(final CompileContext cc) {
    return this;
  }

  /**
   * This method is called by the sequential full-text evaluation.
   * @param qc query context
   * @return resulting item
   * @throws QueryException query exception
   */
  @Override
  public abstract FTNode item(final QueryContext qc, final InputInfo ii) throws QueryException;

  /**
   * This method is called by the index-based full-text evaluation.
   * @param qc query context
   * @return resulting item
   * @throws QueryException query exception
   */
  @Override
  public abstract FTIter iter(final QueryContext qc) throws QueryException;

  @Override
  public boolean has(final Flag flag) {
    for(final FTExpr expr : exprs) if(expr.has(flag)) return true;
    return false;
  }

  @Override
  public boolean removable(final Var var) {
    for(final Expr expr : exprs) if(!expr.removable(var)) return false;
    return true;
  }

  @Override
  public VarUsage count(final Var var) {
    return VarUsage.sum(var, exprs);
  }

  @Override
  public FTExpr inline(final Var var, final Expr ex, final CompileContext cc)
      throws QueryException {
    return inlineAll(exprs, var, ex, cc) ? optimize(cc) : null;
  }

  @Override
  public abstract FTExpr copy(CompileContext cc, IntObjMap<Var> vm);

  /**
   * Checks if sub expressions of a mild not operator violate the grammar.
   * @return result of check
   */
  boolean usesExclude() {
    for(final FTExpr expr : exprs) if(expr.usesExclude()) return true;
    return false;
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(), exprs);
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return visitAll(visitor, exprs);
  }

  @Override
  public int exprSize() {
    int sz = 1;
    for(final Expr expr : exprs) sz += expr.exprSize();
    return sz;
  }

  /**
   * Prints the array with the specified separator.
   * @param sep separator
   * @return string representation
   */
  final String toString(final Object sep) {
    final StringBuilder sb = new StringBuilder();
    final int es = exprs.length;
    for(int e = 0; e < es; e++) sb.append(e == 0 ? "" : sep.toString()).append(exprs[e]);
    return sb.toString();
  }
}
