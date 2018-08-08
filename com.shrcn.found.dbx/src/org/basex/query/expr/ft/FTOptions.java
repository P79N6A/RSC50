package org.basex.query.expr.ft;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.FTIter;
import org.basex.query.value.node.FElem;
import org.basex.query.value.node.FTNode;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.Util;
import org.basex.util.ft.FTOpt;
import org.basex.util.hash.IntObjMap;

/**
 * FTOptions expression.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FTOptions extends FTExpr {
  /** FTOptions. */
  private final FTOpt opt;

  /**
   * Constructor.
   * @param info input info
   * @param expr expression
   * @param opt full-text options
   */
  public FTOptions(final InputInfo info, final FTExpr expr, final FTOpt opt) {
    super(info, expr);
    this.opt = opt;
  }

  @Override
  public FTExpr compile(final CompileContext cc) throws QueryException {
    final QueryContext qc = cc.qc;
    final FTOpt tmp = qc.ftOpt();
    qc.ftOpt(opt.assign(tmp));
    try {
      if(opt.sw != null && qc.value != null && qc.value.data() != null)
        opt.sw.comp(qc.value.data());
      return exprs[0].compile(cc);
    } finally {
      qc.ftOpt(tmp);
    }
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(), opt, exprs[0]);
  }

  @Override
  public String toString() {
    return exprs[0].toString() + opt;
  }

  @Override
  public FTNode item(final QueryContext qc, final InputInfo ii) {
    // shouldn't be called, as compile returns argument
    throw Util.notExpected();
  }

  @Override
  public FTIter iter(final QueryContext qc) {
    // shouldn't be called, as compile returns argument
    throw Util.notExpected();
  }

  @Override
  public FTExpr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new FTOptions(info, exprs[0].copy(cc, vm), new FTOpt().assign(opt));
  }
}
