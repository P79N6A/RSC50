package org.basex.query.func.fn;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Dat;
import org.basex.query.value.item.Dtm;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Tim;
import org.basex.query.value.type.AtomType;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnDateTime extends DateTime {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Item it = exprs[0].atomItem(qc, info);
    final Item zon = exprs.length == 2 ? exprs[1].atomItem(qc, info) : null;
    if(it == null || zon == null) return null;

    final Dat d = it.type.isUntyped() ? new Dat(it.string(info), info) :
      (Dat) checkType(it, AtomType.DAT);
    final Tim t = zon.type.isUntyped() ? new Tim(zon.string(info), info) :
      (Tim) checkType(zon, AtomType.TIM);
    return new Dtm(d, t, info);
  }
}
