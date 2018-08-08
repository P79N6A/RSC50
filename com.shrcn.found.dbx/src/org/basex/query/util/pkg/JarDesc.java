package org.basex.query.util.pkg;

import org.basex.util.list.TokenList;

/**
 * Jar descriptor.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Rositsa Shadura
 */
final class JarDesc {
  /** List of jar files. */
  final TokenList jars = new TokenList();
  /** List of public classes. */
  final TokenList classes = new TokenList();
}
