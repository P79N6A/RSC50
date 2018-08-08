package org.basex.query.func.fn;

import org.basex.query.QueryContext;
import org.basex.query.func.StandardFunc;
import org.basex.query.util.format.Formatter;
import org.basex.query.value.item.Str;
import org.basex.query.value.type.AtomType;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnDefaultLanguage extends StandardFunc {
  @Override
  public Str item(final QueryContext qc, final InputInfo ii) {
    return new Str(Formatter.EN, AtomType.LAN);
  }
}
