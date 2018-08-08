package org.basex.server;

import static org.basex.core.Text.COL;
import static org.basex.core.Text.DOT;
import static org.basex.core.Text.LI;
import static org.basex.core.Text.NL;
import static org.basex.core.Text.SESSIONS_X;

import java.util.concurrent.CopyOnWriteArrayList;

import org.basex.util.TokenBuilder;
import org.basex.util.list.StringList;

/**
 * This class organizes all currently opened database sessions.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Sessions extends CopyOnWriteArrayList<ClientListener> {
  /**
   * Returns information about the currently opened sessions.
   * @return data reference
   */
  public synchronized String info() {
    final TokenBuilder tb = new TokenBuilder();
    tb.addExt(SESSIONS_X, size()).add(size() == 0 ? DOT : COL);

    final StringList sl = new StringList();
    for(final ClientListener sp : this) {
      sl.add(sp.context().user().name() + ' ' + sp);
    }
    for(final String sp : sl.sort()) tb.add(NL).add(LI).add(sp);
    return tb.toString();
  }
}
