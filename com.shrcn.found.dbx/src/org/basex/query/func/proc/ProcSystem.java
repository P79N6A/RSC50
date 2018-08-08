package org.basex.query.func.proc;

import static org.basex.util.Token.string;

import org.basex.query.QueryContext;
import org.basex.query.QueryError.ErrType;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.QNm;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ProcSystem extends ProcFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Result result = exec(qc);
    if(result.code == 0) {
      return Str.get(result.output.normalize().finish());
    }

    // create error message
    final QNm name = new QNm(ErrType.BXPR + String.format("%04d", result.code));
    result.error.normalize();
    throw new QueryException(info, name, string(result.error.normalize().finish()));
  }
}
