package org.basex.query.expr.ft;

import static org.basex.query.QueryText.CURLY1;
import static org.basex.query.QueryText.CURLY2;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Pragma;
import org.basex.query.iter.FTIter;
import org.basex.query.value.node.FElem;
import org.basex.query.value.node.FTNode;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * FTExtensionSelection expression.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public final class FTExtensionSelection extends FTExpr {
  /** Pragmas. */
  private final Pragma[] pragmas;

  /**
   * Constructor.
   * @param info input info
   * @param pragmas pragmas
   * @param expr enclosed FTSelection
   */
  public FTExtensionSelection(final InputInfo info, final Pragma[] pragmas, final FTExpr expr) {
    super(info, expr);
    this.pragmas = pragmas;
  }

  @Override
  public FTNode item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return exprs[0].item(qc, info);
  }

  @Override
  public FTIter iter(final QueryContext qc) throws QueryException {
    return exprs[0].iter(qc);
  }

  @Override
  public FTExpr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    final Pragma[] prag = pragmas.clone();
    final int pl = prag.length;
    for(int i = 0; i < pl; i++) prag[i] = prag[i].copy();
    return copyType(new FTExtensionSelection(info, prag, exprs[0].copy(cc, vm)));
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(), pragmas, exprs);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    for(final Pragma p : pragmas) sb.append(p).append(' ');
    return sb.append(CURLY1 + ' ').append(exprs[0]).append(' ').append(CURLY2).toString();
  }
}
