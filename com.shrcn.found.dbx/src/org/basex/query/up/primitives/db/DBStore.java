package org.basex.query.up.primitives.db;

import static org.basex.query.QueryError.UPDBPUT_X;
import static org.basex.util.Token.string;
import static org.basex.util.Token.token;

import java.io.IOException;

import org.basex.data.Data;
import org.basex.io.IOFile;
import org.basex.io.in.BufferInput;
import org.basex.query.QueryException;
import org.basex.query.func.Function;
import org.basex.query.up.primitives.Update;
import org.basex.query.up.primitives.UpdateType;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.Util;
import org.basex.util.hash.TokenObjMap;

/**
 * Update primitive for the {@link Function#_DB_STORE} function.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DBStore extends DBUpdate {
  /** Keys. */
  private final TokenObjMap<Item> map = new TokenObjMap<>();

  /**
   * Constructor.
   * @param data data
   * @param path target path
   * @param it item to be stored
   * @param inf input info
   */
  public DBStore(final Data data, final String path, final Item it, final InputInfo inf) {
    super(UpdateType.DBSTORE, data, inf);
    map.put(token(path), it);
  }

  @Override
  public void merge(final Update update) {
    final DBStore put = (DBStore) update;
    for(final byte[] path : put.map) map.put(path, put.map.get(path));
  }

  @Override
  public void apply() throws QueryException {
    for(final byte[] path : map) {
      try {
        final IOFile file = data.meta.binary(string(path));
        if(file.isDir()) file.delete();
        file.parent().md();
        try(final BufferInput bi = map.get(path).input(info)) {
          file.write(bi);
        }
      } catch(final IOException ex) {
        Util.debug(ex);
        throw UPDBPUT_X.get(info, path);
      }
    }
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public void prepare() { }
}
