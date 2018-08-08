package org.basex.query.func.fn;

import static org.basex.query.QueryError.SER_X;

import org.basex.io.serial.SerializerOptions;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.FuncOptions;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnSerialize extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Item it = exprs.length > 1 ? exprs[1].item(qc, info) : null;
    final SerializerOptions sopts = FuncOptions.serializer(it, info);
    return Str.get(serialize(exprs[0].iter(qc), sopts, SER_X));
  }
}
