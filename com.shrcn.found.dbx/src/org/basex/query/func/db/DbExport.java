package org.basex.query.func.db;

import static org.basex.query.QueryError.SER_X;
import static org.basex.util.Token.string;

import java.io.IOException;

import org.basex.core.cmd.Export;
import org.basex.data.Data;
import org.basex.io.serial.SerializerOptions;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.FuncOptions;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DbExport extends DbAccess {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    checkCreate(qc);
    final Data data = checkData(qc);
    final String path = string(toToken(exprs[1], qc));
    final Item so = exprs.length > 2 ? exprs[2].item(qc, info) : null;
    final SerializerOptions sopts = FuncOptions.serializer(so, info);
    try {
      Export.export(data, path, sopts, null);
    } catch(final IOException ex) {
      throw SER_X.get(info, ex);
    }
    return null;
  }
}
