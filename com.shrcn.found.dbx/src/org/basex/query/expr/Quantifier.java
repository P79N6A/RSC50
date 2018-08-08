package org.basex.query.expr;

import static org.basex.query.QueryText.EVERY;
import static org.basex.query.QueryText.OPTPRE_X;
import static org.basex.query.QueryText.SOME;
import static org.basex.query.QueryText.TYP;

import java.util.Arrays;
import java.util.LinkedList;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.StaticContext;
import org.basex.query.expr.gflwor.For;
import org.basex.query.expr.gflwor.GFLWOR;
import org.basex.query.expr.gflwor.GFLWOR.Clause;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.Bln;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.FElem;
import org.basex.query.value.type.SeqType;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * Some/Every satisfier clause.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Quantifier extends Single {
  /** Every flag. */
  private final boolean every;

  /**
   * Constructor.
   * @param info input info
   * @param inputs variable inputs
   * @param expr satisfier
   * @param every every flag
   * @param sc static context
   */
  public Quantifier(final InputInfo info, final For[] inputs, final Expr expr,
      final boolean every, final StaticContext sc) {
    this(info, new GFLWOR(info, new LinkedList<Clause>(Arrays.asList(inputs)),
        compBln(expr, info, sc)), every);
  }

  /**
   * Copy constructor.
   * @param info input info
   * @param tests expression
   * @param every every flag
   */
  private Quantifier(final InputInfo info, final Expr tests, final boolean every) {
    super(info, tests);
    this.every = every;
    seqType = SeqType.BLN;
  }

  @Override
  public Expr compile(final CompileContext cc) throws QueryException {
    return super.compile(cc).optimize(cc);
  }

  @Override
  public Expr optimize(final CompileContext cc) throws QueryException {
    // return pre-evaluated result
    if(expr.isValue()) return optPre(item(cc.qc, info), cc);

    // pre-evaluate satisfy clause if it is a value
    if(expr instanceof GFLWOR && !expr.has(Flag.NDT) && !expr.has(Flag.UPD)) {
      final GFLWOR gflwor = (GFLWOR) expr;
      if(gflwor.size() > 0 && gflwor.ret.isValue()) {
        final Value value = (Value) gflwor.ret;
        cc.info(OPTPRE_X, value);
        return Bln.get(value.ebv(cc.qc, info).bool(info));
      }
    }
    return this;
  }

  @Override
  public Bln item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Iter iter = expr.iter(qc);
    for(Item it; (it = iter.next()) != null;) {
      if(every ^ it.ebv(qc, info).bool(info)) return Bln.get(!every);
    }
    return Bln.get(every);
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new Quantifier(info, expr.copy(cc, vm), every);
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(TYP, every ? EVERY : SOME), expr);
  }

  @Override
  public String toString() {
    return (every ? EVERY : SOME) + '(' + expr + ')';
  }

  @Override
  public int exprSize() {
    return expr.exprSize();
  }
}
