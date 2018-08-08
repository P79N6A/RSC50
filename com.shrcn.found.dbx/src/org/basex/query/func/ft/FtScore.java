package org.basex.query.func.ft;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.util.list.ValueList;
import org.basex.query.value.Value;
import org.basex.query.value.item.Dbl;
import org.basex.query.value.item.Item;
import org.basex.query.value.seq.DblSeq;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FtScore extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final boolean s = qc.scoring;
    try {
      qc.scoring = true;
      final Iter iter = exprs[0].iter(qc);
      final ValueList vl = new ValueList(Math.max(1, (int) iter.size()));
      for(Item it; (it = iter.next()) != null;) vl.add(Dbl.get(it.score()));
      return DblSeq.get(vl.finish(), vl.size());
    } finally {
      qc.scoring = s;
    }
  }
}
