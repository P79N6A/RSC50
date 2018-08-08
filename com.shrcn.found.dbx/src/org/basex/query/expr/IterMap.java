package org.basex.query.expr;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * Simple map expression: iterative evaluation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class IterMap extends SimpleMap {
  /**
   * Constructor.
   * @param info input info
   * @param exprs expressions
   */
  IterMap(final InputInfo info, final Expr... exprs) {
    super(info, exprs);
  }

  @Override
  public Iter iter(final QueryContext qc) {
    return new Iter() {
      final int sz = exprs.length;
      final Iter[] iter = new Iter[sz];
      final Value[] values = new Value[sz];
      int pos = -1;

      @Override
      public Item next() throws QueryException {
        final Value cv = qc.value;
        // local copy of variable (faster)
        int p = pos;
        if(p == -1) {
          values[++p] = cv;
          iter[p] = qc.iter(exprs[0]);
        }

        try {
          qc.value = values[p];
          while(true) {
            final Item it = iter[p].next();
            if(it == null) {
              iter[p] = null;
              if(--p == -1) {
                pos = p;
                return null;
              }
            } else if(p < sz - 1) {
              qc.value = it;
              values[++p] = it;
              iter[p] = qc.iter(exprs[p]);
            } else {
              pos = p;
              return it;
            }
          }
        } finally {
          qc.value = cv;
        }
      }
    };
  }

  @Override
  public IterMap copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return copyType(new IterMap(info, Arr.copyAll(cc, vm, exprs)));
  }
}
