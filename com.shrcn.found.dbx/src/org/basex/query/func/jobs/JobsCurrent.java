package org.basex.query.func.jobs;

import org.basex.query.QueryContext;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Str;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class JobsCurrent extends StandardFunc {
  @Override
  public Str item(final QueryContext qc, final InputInfo ii) {
    return Str.get(qc.job().id());
  }
}
