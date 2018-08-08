package org.basex.query.expr;

import static org.basex.query.QueryError.INVCAST_X_X_X;
import static org.basex.query.QueryText.OPTTYPE_X;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryError;
import org.basex.query.QueryException;
import org.basex.query.QueryText;
import org.basex.query.StaticContext;
import org.basex.query.iter.Iter;
import org.basex.query.util.list.ItemList;
import org.basex.query.value.Value;
import org.basex.query.value.item.FuncItem;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.FElem;
import org.basex.query.value.type.FuncType;
import org.basex.query.value.type.SeqType;
import org.basex.query.value.type.SeqType.Occ;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.Token;
import org.basex.util.hash.IntObjMap;

/**
 * Checks the argument expression's result type.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public final class TypeCheck extends Single {
  /** Flag for function conversion. */
  public final boolean promote;
  /** Static context. */
  private final StaticContext sc;

  /**
   * Constructor.
   * @param sc static context
   * @param info input info
   * @param expr expression to be promoted
   * @param seqType type to promote to
   * @param promote flag for function promotion
   */
  public TypeCheck(final StaticContext sc, final InputInfo info, final Expr expr,
      final SeqType seqType, final boolean promote) {
    super(info, expr);
    this.sc = sc;
    this.seqType = seqType;
    this.promote = promote;
  }

  @Override
  public Expr compile(final CompileContext cc) throws QueryException {
    expr = expr.compile(cc);
    return optimize(cc);
  }

  @Override
  public Expr optimize(final CompileContext cc) throws QueryException {
    final SeqType argType = expr.seqType();

    // return type is already correct
    if(argType.instanceOf(seqType)) {
      cc.info(QueryText.OPTTYPE_X, seqType);
      return expr;
    }

    // function item coercion
    if(expr instanceof FuncItem && seqType.type instanceof FuncType) {
      if(!seqType.occ.check(1)) throw QueryError.typeError(expr, seqType, null, info);
      final FuncItem fi = (FuncItem) expr;
      return optPre(fi.coerceTo((FuncType) seqType.type, cc.qc, info, true), cc);
    }

    // we can type check immediately
    if(expr.isValue()) {
      return optPre(value(cc.qc), cc);
    }

    // check at each call
    if(argType.type.instanceOf(seqType.type) && !expr.has(Flag.NDT) && !expr.has(Flag.UPD)) {
      final Occ occ = argType.occ.intersect(seqType.occ);
      if(occ == null) throw QueryError.typeError(expr, seqType, null, info);
    }

    final Expr opt = expr.typeCheck(this, cc);
    if(opt != null) {
      cc.info(OPTTYPE_X, seqType);
      return opt;
    }

    return this;
  }

  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final Iter iter = expr.iter(qc);

    return new Iter() {
      /** Item cache. */
      final ItemList cache = new ItemList();
      /** Item cache index. */
      int c;
      /** Result index. */
      int i;

      @Override
      public Item next() throws QueryException {
        final SeqType st = seqType;
        while(c == cache.size()) {
          qc.checkStop();
          cache.size(0);
          c = 0;

          final Item it = iter.next();
          if(it == null || st.instance(it)) {
            cache.add(it);
          } else {
            st.promote(it, null, cache, qc, sc, info, false);
          }
        }

        final Item it = cache.get(c);
        cache.set(c++, null);

        if(it == null && i < st.occ.min || i > st.occ.max)
          throw QueryError.typeError(TypeCheck.this, st, null, info);

        i++;
        return it;
      }
    };
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final Value val = expr.value(qc);
    if(seqType.instance(val)) return val;
    if(promote) return seqType.promote(val, null, qc, sc, info, false);
    throw INVCAST_X_X_X.get(info, val.seqType(), seqType, val);
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new TypeCheck(sc, info, expr.copy(cc, vm), seqType, promote);
  }

  @Override
  public void plan(final FElem plan) {
    final FElem elem = planElem(QueryText.TYP, seqType);
    if(promote) elem.add(planAttr(QueryText.FUNCTION, Token.TRUE));
    addPlan(plan, elem, expr);
  }

  @Override
  public String toString() {
    return "((: " + seqType + ", " + promote + " :) " + expr + ')';
  }

  /**
   * Checks if this type check is redundant if the result is bound to the given variable.
   * @param var variable
   * @return result of check
   */
  public boolean isRedundant(final Var var) {
    return (!promote || var.promotes()) && var.declaredType().instanceOf(seqType);
  }

  /**
   * Creates an expression that checks the given expression's return type.
   * @param ex expression to check
   * @param cc compilation context
   * @return the resulting expression
   * @throws QueryException query exception
   */
  public Expr check(final Expr ex, final CompileContext cc) throws QueryException {
    return new TypeCheck(sc, info, ex, seqType, promote).optimize(cc);
  }
}
