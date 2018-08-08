package org.basex.query.func.proc;

import java.util.Map.Entry;

import org.basex.query.QueryContext;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.seq.StrSeq;
import org.basex.util.Prop;
import org.basex.util.list.TokenList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ProcPropertyNames extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) {
    final TokenList tl = new TokenList();
    for(final Entry<String, String> entry : Prop.entries()) tl.add(entry.getKey());
    return StrSeq.get(tl.sort());
  }
}
