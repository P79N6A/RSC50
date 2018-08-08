package org.basex.query.func.db;

import static org.basex.query.QueryError.WHICHRES_X;
import static org.basex.util.Token.string;

import org.basex.data.Data;
import org.basex.io.IOFile;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;
import org.basex.util.http.MediaType;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbContentType extends DbAccess {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Data data = checkData(qc);
    final String path = path(1, qc);
    final int pre = data.resources.doc(path);
    MediaType type = null;
    if(pre != -1) {
      // check media type; return application/xml if returned string is not of type xml
      type = MediaType.get(string(data.text(pre, true)));
      if(!type.isXML()) type = MediaType.APPLICATION_XML;
    } else if(!data.inMemory()) {
      final IOFile io = data.meta.binary(path);
      if(io.exists() && !io.isDir()) type = MediaType.get(path);
    }
    if(type == null) throw WHICHRES_X.get(info, path);
    return Str.get(type.toString());
  }
}
