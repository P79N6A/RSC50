package org.basex.query.func.client;

import java.io.IOException;

import org.basex.api.client.ClientSession;
import org.basex.query.QueryResource;
import org.basex.query.value.item.Uri;
import org.basex.util.Prop;
import org.basex.util.Token;
import org.basex.util.Util;
import org.basex.util.hash.TokenObjMap;

/**
 * Opened database client sessions.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ClientSessions implements QueryResource {
  /** Last inserted id. */
  private int lastId = -1;
  /** Map with all open sessions and their ids. */
  private final TokenObjMap<ClientSession> conns = new TokenObjMap<>();

  /**
   * Adds a session.
   * @param cs client session
   * @return session id
   */
  synchronized Uri add(final ClientSession cs) {
    final byte[] uri = Token.token(Prop.PROJECT_NAME + "://" + cs + '/' + ++lastId);
    conns.put(uri, cs);
    return Uri.uri(uri);
  }

  /**
   * Returns a session.
   * @param id session id
   * @return session
   */
  synchronized ClientSession get(final Uri id) {
    return conns.get(id.string());
  }

  /**
   * Removes a session.
   * @param id session id
   */
  synchronized void remove(final Uri id) {
    conns.delete(id.string());
  }

  @Override
  public void close() {
    for(final ClientSession cs : conns.values()) {
      try {
        if(cs != null) cs.close();
      } catch(final IOException ex) {
        Util.debug(ex);
      }
    }
  }
}
