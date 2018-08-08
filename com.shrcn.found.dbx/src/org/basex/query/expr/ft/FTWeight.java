package org.basex.query.expr.ft;

import static org.basex.query.QueryError.FTWEIGHT_X;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryText;
import org.basex.query.expr.Expr;
import org.basex.query.iter.FTIter;
import org.basex.query.util.ASTVisitor;
import org.basex.query.util.IndexInfo;
import org.basex.query.value.node.FElem;
import org.basex.query.value.node.FTNode;
import org.basex.query.var.Var;
import org.basex.query.var.VarUsage;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * FTOptions expression.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FTWeight extends FTExpr {
  /** Weight. */
  private Expr weight;

  /**
   * Constructor.
   * @param info input info
   * @param expr expression
   * @param weight weight
   */
  public FTWeight(final InputInfo info, final FTExpr expr, final Expr weight) {
    super(info, expr);
    this.weight = weight;
  }

  @Override
  public void checkUp() throws QueryException {
    checkNoUp(weight);
    super.checkUp();
  }

  @Override
  public FTExpr compile(final CompileContext cc) throws QueryException {
    weight = weight.compile(cc);
    return super.compile(cc);
  }

  // called by sequential variant
  @Override
  public FTNode item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return weight(exprs[0].item(qc, info), qc);
  }

  // called by index variant
  @Override
  public FTIter iter(final QueryContext qc) {
    return new FTIter() {
      @Override
      public FTNode next() throws QueryException {
        return weight(exprs[0].iter(qc).next(), qc);
      }
    };
  }

  /**
   * Returns the item with weight calculation.
   * @param item input item
   * @param qc query context
   * @return item
   * @throws QueryException query exception
   */
  private FTNode weight(final FTNode item, final QueryContext qc) throws QueryException {
    // evaluate weight
    if(item == null) return null;
    final double d = toDouble(weight, qc);
    if(Math.abs(d) > 1000) throw FTWEIGHT_X.get(info, d);
    if(d == 0) item.matches().reset();
    item.score(item.score() * d);
    return item;
  }

  @Override
  public boolean indexAccessible(final IndexInfo ii) {
    // weight makes no sense as long as no index-based scoring exists
    return false;
  }

  @Override
  public boolean has(final Flag flag) {
    return weight.has(flag) || super.has(flag);
  }

  @Override
  public boolean removable(final Var var) {
    return weight.removable(var) && super.removable(var);
  }

  @Override
  public VarUsage count(final Var var) {
    return weight.count(var).plus(super.count(var));
  }

  @Override
  public FTExpr inline(final Var var, final Expr ex, final CompileContext cc)
      throws QueryException {
    boolean change = inlineAll(exprs, var, ex, cc);
    final Expr w = weight.inline(var, ex, cc);
    if(w != null) {
      weight = w;
      change = true;
    }
    return change ? optimize(cc) : null;
  }

  @Override
  public FTExpr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new FTWeight(info, exprs[0].copy(cc, vm), weight.copy(cc, vm));
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(), weight, exprs[0]);
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return super.accept(visitor) && weight.accept(visitor);
  }

  @Override
  public int exprSize() {
    int sz = 1;
    for(final FTExpr expr : exprs) sz += expr.exprSize();
    return sz + weight.exprSize();
  }

  @Override
  public String toString() {
    return exprs[0] + " " + QueryText.WEIGHT + " {" + weight + "} ";
  }
}
