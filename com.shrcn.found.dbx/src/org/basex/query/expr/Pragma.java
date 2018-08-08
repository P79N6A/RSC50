package org.basex.query.expr;

import static org.basex.query.QueryText.PRAGMA;
import static org.basex.query.QueryText.PRAGMA2;
import static org.basex.query.QueryText.VAL;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr.Flag;
import org.basex.query.value.item.QNm;
import org.basex.query.value.node.FElem;
import org.basex.util.InputInfo;
import org.basex.util.TokenBuilder;

/**
 * Abstract pragma expression.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public abstract class Pragma extends ExprInfo {
  /** QName. */
  final QNm name;
  /** Pragma value. */
  final byte[] value;

  /**
   * Constructor.
   * @param name name of pragma
   * @param value optional value
   */
  Pragma(final QNm name, final byte[] value) {
    this.name = name;
    this.value = value;
  }

  @Override
  public final void plan(final FElem plan) {
    addPlan(plan, planElem(VAL, value), name);
  }

  /**
   * Initializes the pragma expression.
   * @param qc query context
   * @param info input info
   * @return cached information
   * @throws QueryException query exception
   */
  abstract Object init(final QueryContext qc, final InputInfo info) throws QueryException;

  /**
   * Finalizes the pragma expression.
   * @param qc query context
   * @param cache TODO
   */
  abstract void finish(final QueryContext qc, Object cache);

  /**
   * Indicates if an expression has the specified compiler property.
   * @param flag flag to be checked
   * @return result of check
   * @see Expr#has
   */
  public abstract boolean has(final Flag flag);

  @Override
  public final String toString() {
    final TokenBuilder tb = new TokenBuilder(PRAGMA + ' ' + name + ' ');
    if(value.length != 0) tb.add(value).add(' ');
    return tb.add(PRAGMA2).toString();
  }

  /**
   * Creates a copy of this pragma.
   * @return copy
   */
  public abstract Pragma copy();
}
