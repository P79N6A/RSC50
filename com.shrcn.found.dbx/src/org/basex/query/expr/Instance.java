package org.basex.query.expr;

import static org.basex.query.QueryText.TYP;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Bln;
import org.basex.query.value.node.FElem;
import org.basex.query.value.type.SeqType;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.Util;
import org.basex.util.hash.IntObjMap;

/**
 * Instance test.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Instance extends Single {
  /** Sequence type to check for. */
  private final SeqType type;

  /**
   * Constructor.
   * @param info input info
   * @param expr expression
   * @param type sequence type to check for
   */
  public Instance(final InputInfo info, final Expr expr, final SeqType type) {
    super(info, expr);
    this.type = type;
    seqType = SeqType.BLN;
  }

  @Override
  public Expr compile(final CompileContext cc) throws QueryException {
    return super.compile(cc).optimize(cc);
  }

  @Override
  public Expr optimize(final CompileContext cc) throws QueryException {
    return expr.isValue() ? preEval(cc) : this;
  }

  @Override
  public Bln item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return Bln.get(type.instance(qc.value(expr)));
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new Instance(info, expr.copy(cc, vm), type);
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(TYP, type), expr);
  }

  @Override
  public String toString() {
    return Util.info("% instance of %", expr, type);
  }
}
