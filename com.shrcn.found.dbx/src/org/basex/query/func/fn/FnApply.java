package org.basex.query.func.fn;

import static org.basex.query.QueryError.APPLY_X_X;
import static org.basex.query.QueryError.FUNCUP_X;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.ann.Annotation;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.util.list.ValueList;
import org.basex.query.value.Value;
import org.basex.query.value.array.Array;
import org.basex.query.value.item.FItem;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnApply extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final FItem fun = toFunc(exprs[0], qc);
    if(!sc.mixUpdates && fun.annotations().contains(Annotation.UPDATING))
      throw FUNCUP_X.get(info, fun);

    final Array array = toArray(exprs[1], qc);
    final long as = array.arraySize();
    if(fun.arity() != as) throw APPLY_X_X.get(info, fun.arity(), as);

    final ValueList vl = new ValueList((int) as);
    for(final Value val : array.members()) vl.add(val);
    return fun.invokeValue(qc, info, vl.finish());
  }
}
