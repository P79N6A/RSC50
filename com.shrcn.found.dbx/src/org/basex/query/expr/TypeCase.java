package org.basex.query.expr;

import static org.basex.query.QueryText.AS;
import static org.basex.query.QueryText.CASE;
import static org.basex.query.QueryText.DEFAULT;
import static org.basex.query.QueryText.RETURN;
import static org.basex.query.QueryText.TYPE;
import static org.basex.query.QueryText.VAR;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.util.ASTVisitor;
import org.basex.query.value.Value;
import org.basex.query.value.node.FElem;
import org.basex.query.value.type.SeqType;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.Token;
import org.basex.util.TokenBuilder;
import org.basex.util.hash.IntObjMap;
import org.basex.util.list.ByteList;

/**
 * Case expression for typeswitch.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class TypeCase extends Single {
  /** Variable. */
  final Var var;
  /** Matched sequence types. */
  private final SeqType[] types;

  /**
   * Constructor.
   * @param info input info
   * @param var variable
   * @param types sequence types this case matches, the empty array means {@code default}
   * @param expr return expression
   */
  public TypeCase(final InputInfo info, final Var var, final SeqType[] types, final Expr expr) {
    super(info, expr);
    this.var = var;
    this.types = types;
  }

  @Override
  public TypeCase compile(final CompileContext cc) throws QueryException {
    return compile(cc, null);
  }

  /**
   * Compiles the expression.
   * @param cc compilation context
   * @param v value to be bound
   * @return resulting item
   * @throws QueryException query exception
   */
  TypeCase compile(final CompileContext cc, final Value v) throws QueryException {
    final Value val = var != null && v != null ? var.checkType(v, cc.qc, true) : null;
    try {
      super.compile(cc);
      if(val != null) {
        final Expr inlined = expr.inline(var, val, cc);
        if(inlined != null) expr = inlined;
      }
    } catch(final QueryException ex) {
      // replace original expression with error
      expr = cc.error(ex, expr);
    }
    return optimize(cc);
  }

  @Override
  public TypeCase optimize(final CompileContext cc) {
    seqType = expr.seqType();
    return this;
  }

  @Override
  public Expr inline(final Var v, final Expr ex, final CompileContext cc) {
    try {
      return super.inline(v, ex, cc);
    } catch(final QueryException qe) {
      expr = cc.error(qe, expr);
      return this;
    }
  }

  @Override
  public TypeCase copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new TypeCase(info, cc.copy(var, vm), types.clone(), expr.copy(cc, vm));
  }

  /**
   * Checks if the given value matches this case.
   * @param val value to be matched
   * @return {@code true} if it matches, {@code false} otherwise
   */
  boolean matches(final Value val) {
    if(types.length == 0) return true;
    for(final SeqType t : types) if(t.instance(val)) return true;
    return false;
  }

  /**
   * Evaluates the expression.
   * @param qc query context
   * @param seq sequence to be checked
   * @return resulting item
   * @throws QueryException query exception
   */
  Iter iter(final QueryContext qc, final Value seq) throws QueryException {
    if(!matches(seq)) return null;

    if(var == null) return qc.iter(expr);
    qc.set(var, seq);
    return qc.value(expr).iter();
  }

  @Override
  public void plan(final FElem plan) {
    final FElem e = planElem();
    if(types.length == 0) {
      e.add(planAttr(Token.token(DEFAULT), Token.TRUE));
    } else {
      final byte[] or = { ' ', '|', ' ' };
      final ByteList bl = new ByteList();
      for(final SeqType t : types) {
        if(!bl.isEmpty()) bl.add(or);
        bl.add(Token.token(t.toString()));
      }
      e.add(planAttr(Token.token(TYPE), bl.finish()));
    }
    if(var != null) e.add(planAttr(VAR, Token.token(var.toString())));
    expr.plan(e);
    plan.add(e);
  }

  @Override
  public String toString() {
    final int tl = types.length;
    final TokenBuilder tb = new TokenBuilder(tl == 0 ? DEFAULT : CASE);
    if(var != null) {
      tb.add(' ').add(var.toString());
      if(tl != 0) tb.add(' ').add(AS);
    }
    if(tl != 0) {
      for(int t = 0; t < tl; t++) {
        if(t > 0) tb.add(" |");
        tb.add(' ').add(types[t].toString());
      }
    }
    return tb.add(' ' + RETURN + ' ' + expr).toString();
  }

  @Override
  public void markTailCalls(final CompileContext cc) {
    expr.markTailCalls(cc);
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return super.accept(visitor) && (var == null || visitor.declared(var));
  }

  @Override
  public int exprSize() {
    return expr.exprSize();
  }
}
