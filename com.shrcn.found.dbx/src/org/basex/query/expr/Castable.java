package org.basex.query.expr;

import static org.basex.query.QueryText.AS;
import static org.basex.query.QueryText.CASTABLE;
import static org.basex.query.QueryText.TYP;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.StaticContext;
import org.basex.query.value.Value;
import org.basex.query.value.item.Bln;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.FElem;
import org.basex.query.value.type.SeqType;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * Castable expression.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Castable extends Single {
  /** Static context. */
  private final StaticContext sc;
  /** Sequence type to check for. */
  private final SeqType type;

  /**
   * Constructor.
   * @param sc static context
   * @param info input info
   * @param expr expression
   * @param type sequence type to check for
   */
  public Castable(final StaticContext sc, final InputInfo info, final Expr expr,
      final SeqType type) {
    super(info, expr);
    this.sc = sc;
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
    final Value v = expr.value(qc);
    return Bln.get(type.occ.check(v.size()) &&
        (v.isEmpty() || type.cast((Item) v, qc, sc, info, false) != null));
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new Castable(sc, info, expr.copy(cc, vm), type);
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(TYP, type), expr);
  }

  @Override
  public String toString() {
    return expr + " " + CASTABLE + ' ' + AS + ' ' + type;
  }
}
