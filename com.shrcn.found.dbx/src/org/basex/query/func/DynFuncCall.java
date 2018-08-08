package org.basex.query.func;

import static org.basex.query.QueryError.FUNCNOTUP;
import static org.basex.query.QueryError.FUNCUP;
import static org.basex.query.QueryError.INVARITY_X_X_X;
import static org.basex.query.QueryError.INVFUNCITEM_X_X;
import static org.basex.query.QueryError.arguments;
import static org.basex.query.QueryText.TCL;

import java.util.Arrays;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.StaticContext;
import org.basex.query.ann.Annotation;
import org.basex.query.expr.Expr;
import org.basex.query.expr.XQFunctionExpr;
import org.basex.query.util.ASTVisitor;
import org.basex.query.util.list.ExprList;
import org.basex.query.value.Value;
import org.basex.query.value.item.FItem;
import org.basex.query.value.item.FuncItem;
import org.basex.query.value.item.Item;
import org.basex.query.value.map.Map;
import org.basex.query.value.node.FElem;
import org.basex.query.value.type.FuncType;
import org.basex.query.value.type.MapType;
import org.basex.query.value.type.SeqType;
import org.basex.query.value.type.SeqType.Occ;
import org.basex.query.value.type.Type;
import org.basex.query.var.Var;
import org.basex.util.Array;
import org.basex.util.InputInfo;
import org.basex.util.TokenBuilder;
import org.basex.util.hash.IntObjMap;

/**
 * Dynamic function call.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public final class DynFuncCall extends FuncCall {
  /** Static context. */
  private final StaticContext sc;
  /** Updating flag. */
  private final boolean upd;

  /** Non-deterministic flag. */
  private boolean ndt;
  /** Hash values of all function items that this call was copied from, possibly {@code null}. */
  private int[] inlinedFrom;

  /**
   * Function constructor.
   * @param info input info
   * @param sc static context
   * @param expr function expression
   * @param arg arguments
   */
  public DynFuncCall(final InputInfo info, final StaticContext sc, final Expr expr,
      final Expr... arg) {
    this(info, sc, false, false, expr, arg);
  }

  /**
   * Function constructor.
   * @param info input info
   * @param sc static context
   * @param upd updating flag
   * @param ndt non-deterministic flag
   * @param expr function expression
   * @param arg arguments
   */
  public DynFuncCall(final InputInfo info, final StaticContext sc, final boolean upd,
      final boolean ndt, final Expr expr, final Expr... arg) {

    super(info, ExprList.concat(arg, expr));
    this.sc = sc;
    this.upd = upd;
    this.ndt = ndt;
    sc.dynFuncCall = true;
  }

  @Override
  public Expr compile(final CompileContext cc) throws QueryException {
    if(body().has(Flag.NDT)) ndt = true;
    return super.compile(cc);
  }

  @Override
  public Expr optimize(final CompileContext cc) throws QueryException {
    final Expr f = body();
    final Type tp = f.seqType().type;

    final int nargs = exprs.length - 1;
    if(tp instanceof FuncType) {
      final FuncType ft = (FuncType) tp;
      if(ft.argTypes != null && ft.argTypes.length != nargs) throw INVARITY_X_X_X.get(
          info, arguments(nargs), ft.argTypes.length, f.toErrorString());
      if(ft.type != null) {
        SeqType rt = ft.type;
        if(tp instanceof MapType && !rt.mayBeZero())
          rt = rt.withOcc(rt.one() ? Occ.ZERO_ONE : Occ.ZERO_MORE);
        seqType = rt;
      }
    }

    // maps and arrays can only contain fully evaluated values, so this is safe
    if((f instanceof Map || f instanceof org.basex.query.value.array.Array) && allAreValues())
      return optPre(value(cc.qc), cc);

    if(f instanceof XQFunctionExpr) {
      // try to inline the function
      final XQFunctionExpr fe = (XQFunctionExpr) f;
      if(!(f instanceof FuncItem && comesFrom((FuncItem) f))) {
        checkUpdating(fe);
        final Expr[] args = Arrays.copyOf(exprs, nargs);
        final Expr in = fe.inlineExpr(args, cc, info);
        if(in != null) return in;
      }
    } else if(f instanceof Item) {
      throw INVFUNCITEM_X_X.get(info, ((Item) f).type, f);
    }
    return this;
  }

  @Override
  public void checkUp() throws QueryException {
    checkNoneUp(Arrays.copyOf(exprs, exprs.length - 1));
    body().checkUp();
  }

  /**
   * Marks this call after it was inlined from the given function item.
   * @param it the function item
   */
  public void markInlined(final FuncItem it) {
    final int hash = it.hashCode();
    inlinedFrom = inlinedFrom == null ? new int[] { hash } : Array.add(inlinedFrom, hash);
  }

  /**
   * Checks if this call was inlined from the body of the given function item.
   * @param it function item
   * @return result of check
   */
  private boolean comesFrom(final FuncItem it) {
    if(inlinedFrom != null) {
      final int hash = it.hashCode();
      for(final int h : inlinedFrom) {
        if(hash == h) return true;
      }
    }
    return false;
  }

  /**
   * Returns the function body expression.
   * @return body
   */
  private Expr body() {
    return exprs[exprs.length - 1];
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    final Expr[] copy = copyAll(cc, vm, exprs);
    final int last = copy.length - 1;
    final Expr[] args = Arrays.copyOf(copy, last);
    final DynFuncCall call = new DynFuncCall(info, sc, upd, ndt, copy[last], args);
    if(inlinedFrom != null) call.inlinedFrom = inlinedFrom.clone();
    return copyType(call);
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return visitor.dynFuncCall(this) && visitAll(visitor, exprs);
  }

  @Override
  public void plan(final FElem plan) {
    final FElem el = planElem(TCL, tailCall);
    addPlan(plan, el, body());
    final int last = exprs.length - 1;
    for(int e = 0; e < last; e++) exprs[e].plan(el);
  }

  @Override
  public String description() {
    return body().description() + "(...)";
  }

  @Override
  FItem evalFunc(final QueryContext qc) throws QueryException {
    final Item it = toItem(body(), qc);
    if(!(it instanceof FItem)) throw INVFUNCITEM_X_X.get(info, it.type, it);

    final FItem f = (FItem) it;
    final int nargs = exprs.length - 1;
    if(f.arity() != nargs) throw INVARITY_X_X_X.get(
        info, arguments(nargs), f.arity(), f.toErrorString());
    checkUpdating(f);
    return f;
  }

  /**
   * Checks if the function is updating or not.
   * @param item function expression
   * @throws QueryException query exception
   */
  private void checkUpdating(final XQFunctionExpr item) throws QueryException {
    if(!sc.mixUpdates && upd != item.annotations().contains(Annotation.UPDATING))
      throw (upd ? FUNCNOTUP : FUNCUP).get(info);
  }

  @Override
  Value[] evalArgs(final QueryContext qc) throws QueryException {
    final int last = exprs.length - 1;
    final Value[] args = new Value[last];
    for(int a = 0; a < last; a++) args[a] = qc.value(exprs[a]);
    return args;
  }

  @Override
  public boolean has(final Flag flag) {
    return flag == Flag.UPD ? upd : flag == Flag.NDT ? ndt : super.has(flag);
  }

  @Override
  public String toString() {
    final TokenBuilder tb = new TokenBuilder(body().toString()).add('(');
    final int last = exprs.length - 1;
    for(int e = 0; e < last; e++) {
      tb.add(exprs[e].toString());
      if(e < last - 1) tb.add(", ");
    }
    return tb.add(')').toString();
  }
}
