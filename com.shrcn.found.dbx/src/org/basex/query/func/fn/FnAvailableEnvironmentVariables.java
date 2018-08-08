package org.basex.query.func.fn;

import org.basex.query.QueryContext;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.Str;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnAvailableEnvironmentVariables extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) {
    final ValueBuilder vb = new ValueBuilder();
    for(final Object k : System.getenv().keySet()) vb.add(Str.get(k.toString()));
    return vb.value();
  }
}
