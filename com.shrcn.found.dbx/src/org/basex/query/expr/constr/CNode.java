package org.basex.query.expr.constr;

import static org.basex.query.QueryText.SEP;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.StaticContext;
import org.basex.query.expr.Arr;
import org.basex.query.expr.Expr;
import org.basex.query.value.node.ANode;
import org.basex.util.InputInfo;

/**
 * Node constructor.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public abstract class CNode extends Arr {
  /** Static context. */
  final StaticContext sc;

  /**
   * Constructor.
   * @param sc static context
   * @param info input info
   * @param exprs expressions
   */
  CNode(final StaticContext sc, final InputInfo info, final Expr... exprs) {
    super(info, exprs);
    this.sc = sc;
    size = 1;
  }

  @Override
  public abstract ANode item(final QueryContext qc, final InputInfo ii) throws QueryException;

  @Override
  public boolean has(final Flag flag) {
    return flag == Flag.CNS || super.has(flag);
  }

  /**
   * Returns a string info for the expression.
   * @param pref info prefix
   * @return string
   */
  static String info(final String pref) {
    return pref + " constructor";
  }

  @Override
  protected String toString(final String pref) {
    return pref + " { " + super.toString(SEP) + " }";
  }
}
