package org.basex.query.util.parse;

import java.util.HashMap;

import org.basex.query.StaticContext;
import org.basex.query.expr.Expr;
import org.basex.query.var.Var;
import org.basex.query.var.VarScope;
import org.basex.query.var.VarStack;

/**
 * Variable context for resolving local variables.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class VarContext {
  /** Stack of local variables. */
  final VarStack stack = new VarStack();
  /** Non-local variable bindings for closures. */
  final HashMap<Var, Expr> bindings;
  /** Current scope containing all variables and the closure. */
  final VarScope vs;

  /**
   * Constructor.
   * @param bindings non-local variable bindings for closures
   * @param sc static context
   */
  VarContext(final HashMap<Var, Expr> bindings, final StaticContext sc) {
    this.bindings = bindings;
    vs = new VarScope(sc);
  }

  /**
   * Adds a new variable to this context.
   * @param var variable
   */
  void add(final Var var) {
    vs.add(var);
    stack.push(var);
  }
}
