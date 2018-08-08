package org.basex.query.func.csv;

import static org.basex.query.QueryError.INVALIDOPT_X;

import org.basex.build.csv.CsvOptions;
import org.basex.io.serial.SerialMethod;
import org.basex.io.serial.SerializerOptions;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class CsvSerialize extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Iter iter = qc.iter(exprs[0]);
    final CsvOptions copts = toOptions(1, new CsvOptions(), qc);

    final SerializerOptions sopts = new SerializerOptions();
    sopts.set(SerializerOptions.METHOD, SerialMethod.CSV);
    sopts.set(SerializerOptions.CSV, copts);
    return Str.get(serialize(iter, sopts, INVALIDOPT_X));
  }
}
