package org.basex.query.var;

import static org.basex.query.QueryError.VARPRIVATE_X;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryText;
import org.basex.query.StaticContext;
import org.basex.query.ann.Annotation;
import org.basex.query.expr.Expr;
import org.basex.query.expr.ParseExpr;
import org.basex.query.iter.Iter;
import org.basex.query.util.ASTVisitor;
import org.basex.query.value.Value;
import org.basex.query.value.item.QNm;
import org.basex.query.value.node.FElem;
import org.basex.util.InputInfo;
import org.basex.util.Token;
import org.basex.util.hash.IntObjMap;

/**
 * Reference to a static variable.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
final class StaticVarRef extends ParseExpr {
  /** Variable name. */
  private final QNm name;
  /** Referenced variable. */
  private StaticVar var;
  /** URI of the enclosing module. */
  private final StaticContext sc;

  /**
   * Constructor.
   * @param info input info
   * @param name variable name
   * @param sc static context
   */
  StaticVarRef(final InputInfo info, final QNm name, final StaticContext sc) {
    super(info);
    this.name = name;
    this.sc = sc;
  }

  @Override
  public void checkUp() {
  }

  @Override
  public Expr compile(final CompileContext cc) throws QueryException {
    var.comp(cc);
    seqType = var.seqType();
    return var.val != null ? var.val : this;
  }

  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    return var.value(qc);
  }

  @Override
  public boolean has(final Flag flag) {
    return var != null && var.has(flag);
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return visitor.staticVar(var);
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    final StaticVarRef ref = new StaticVarRef(info, name, sc);
    ref.var = var;
    return ref;
  }

  @Override
  public int exprSize() {
    // should always be inlined
    return 0;
  }

  @Override
  public String toString() {
    return '$' + Token.string(name.string());
  }

  @Override
  public boolean removable(final Var v) {
    return true;
  }

  @Override
  public VarUsage count(final Var v) {
    return VarUsage.NEVER;
  }

  @Override
  public Expr inline(final Var v, final Expr ex, final CompileContext cc) {
    return null;
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(QueryText.VAR, name));
  }

  /**
   * Initializes this reference with the given variable.
   * @param vr variable
   * @throws QueryException query exception
   */
  void init(final StaticVar vr) throws QueryException {
    if(vr.anns.contains(Annotation.PRIVATE) && !sc.baseURI().eq(vr.sc.baseURI()))
      throw VARPRIVATE_X.get(info, vr);
    var = vr;
  }
}
