package org.basex.query.expr;

import static org.basex.query.QueryText.OPTFLAT_X_X;
import static org.basex.query.QueryText.OPTREMOVE_X_X;

import org.basex.query.CompileContext;
import org.basex.query.QueryException;
import org.basex.query.util.list.ExprList;
import org.basex.query.value.item.Bln;
import org.basex.query.value.node.FElem;
import org.basex.query.value.type.SeqType;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;

/**
 * Logical expression, extended by {@link And} and {@link Or}.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
abstract class Logical extends Arr {
  /**
   * Constructor.
   * @param info input info
   * @param exprs expressions
   */
  Logical(final InputInfo info, final Expr[] exprs) {
    super(info, exprs);
    seqType = SeqType.BLN;
  }

  @Override
  public final Expr compile(final CompileContext cc) throws QueryException {
    for(int i = 0; i < exprs.length; i++) {
      try {
        exprs[i] = exprs[i].compile(cc);
      } catch(final QueryException qe) {
        // first expression is evaluated eagerly
        if(i == 0) throw qe;
        exprs[i] = cc.error(qe, exprs[i]);
      }
    }
    return optimize(cc);
  }

  @Override
  public Expr optimize(final CompileContext cc) throws QueryException {
    final boolean and = this instanceof And;
    final int es = exprs.length;
    final ExprList el = new ExprList(es);
    for(final Expr expr : exprs) {
      final Expr ex = expr.optimizeEbv(cc);
      if(ex.isValue()) {
        // atomic items can be pre-evaluated
        cc.info(OPTREMOVE_X_X, this, expr);
        if(ex.ebv(cc.qc, info).bool(info) ^ and) return Bln.get(!and);
      } else {
        el.add(ex);
      }
    }
    if(el.isEmpty()) return Bln.get(and);
    exprs = el.finish();
    return this;
  }

  @Override
  public final void markTailCalls(final CompileContext cc) {
    // if the last expression surely returns a boolean, we can jump to it
    final Expr last = exprs[exprs.length - 1];
    if(last.seqType().eq(SeqType.BLN)) last.markTailCalls(cc);
  }

  @Override
  public void plan(final FElem plan) {
    final FElem el = planElem();
    plan.add(el);
    for(final ExprInfo expr : exprs) if(expr != null) expr.plan(el);
  }

  @Override
  public Expr inline(final Var var, final Expr ex, final CompileContext cc) throws QueryException {
    final Expr[] arr = exprs;
    boolean change = false;
    for(int i = 0; i < arr.length; i++) {
      try {
        final Expr e = arr[i].inline(var, ex, cc);
        if(e != null) {
          arr[i] = e;
          change = true;
        }
      } catch(final QueryException qe) {
        // first expression is evaluated eagerly
        if(i == 0) throw qe;

        // everything behind the error is dead anyway
        final Expr[] nw = new Expr[i + 1];
        System.arraycopy(arr, 0, nw, 0, i);
        nw[i] = cc.error(qe, this);
        exprs = nw;
        change = true;
        break;
      }
    }
    return change ? optimize(cc) : null;
  }

  /**
   * Flattens nested logical expressions.
   * @param cc compilation context
   */
  final void compFlatten(final CompileContext cc) {
    // flatten nested expressions
    final ExprList tmp = new ExprList(exprs.length);
    final boolean and = this instanceof And;
    final boolean or = this instanceof Or;
    for(final Expr ex : exprs) {
      if(and && ex instanceof And || or && ex instanceof Or) {
        for(final Expr e : ((Arr) ex).exprs) tmp.add(e);
        cc.info(OPTFLAT_X_X, description(), ex);
      } else {
        tmp.add(ex);
      }
    }
    exprs = tmp.finish();
  }
}
