package org.basex.query.util.parse;

import org.basex.util.InputInfo;
import org.basex.util.list.TokenList;

/**
 * Information required for parsing a module.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ModInfo {
  /** Paths. */
  public final TokenList paths = new TokenList(1);
  /** URI. */
  public byte[] uri;
  /** Input info. */
  public InputInfo info;
}
