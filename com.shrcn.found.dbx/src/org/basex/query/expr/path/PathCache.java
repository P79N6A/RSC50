package org.basex.query.expr.path;

import org.basex.query.expr.Expr;
import org.basex.query.expr.Root;
import org.basex.query.value.Value;
import org.basex.query.value.node.ANode;

/**
 * Cache for path results.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class PathCache {
  /** Caching states. */
  enum State {
    /** Initialize caching.  */ INIT,
    /** Caching is possible. */ ENABLED,
    /** Ready to cache.      */ READY,
    /** Results are cached.  */ CACHED,
    /** Caching is disabled. */ DISABLED
  }

  /** Current state. */
  State state = State.INIT;
  /** Cached result. */
  Value result;
  /** Initial context value. */
  Value initial;


  /**
   * Checks if the specified context value is different to the cached one.
   * @param value current context value
   * @param root root expression
   * @return result of check
   */
  boolean sameContext(final Value value, final Expr root) {
    // context value has not changed...
    if(value == initial && (value == null || value.sameAs(initial))) return true;
    // otherwise, if path starts with root node, compare roots of cached and new context value
    return root instanceof Root && value instanceof ANode && initial instanceof ANode &&
        ((ANode) initial).root().sameAs(((ANode) value).root());
  }
}
