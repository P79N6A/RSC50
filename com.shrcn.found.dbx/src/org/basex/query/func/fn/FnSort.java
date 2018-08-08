package org.basex.query.func.fn;

import static org.basex.query.QueryError.FIEQ_X;
import static org.basex.query.QueryError.diffError;

import java.util.Arrays;
import java.util.Comparator;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryRTException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.iter.ValueIter;
import org.basex.query.util.list.ValueList;
import org.basex.query.value.Value;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.Dbl;
import org.basex.query.value.item.FItem;
import org.basex.query.value.item.Flt;
import org.basex.query.value.item.Item;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnSort extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final Value value = exprs[0].value(qc);

    final int sz = (int) value.size();
    final ValueList vl = new ValueList(sz);
    if(exprs.length > 1) {
      final FItem key = checkArity(exprs[1], 1, qc);
      for(final Item it : value) vl.add(key.invokeValue(qc, info, it));
    } else {
      for(final Item it : value) vl.add(it.atomValue(qc, info));
    }

    final Integer[] order = sort(vl, this);
    return new ValueIter() {
      int c;
      @Override
      public Item get(final long i) { return value.itemAt(order[(int) i]); }
      @Override
      public Item next() { return c < sz ? get(c++) : null; }
      @Override
      public long size() { return sz; }
      @Override
      public Value value() {
        final ValueBuilder vb = new ValueBuilder();
        for(int r = 0; r < sz; r++) vb.add(get(r));
        return vb.value();
      }
    };
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    return iter(qc).value();
  }

  /**
   * Sort the input data.
   * @param vl value list.
   * @param sf calling function
   * @return item order
   * @throws QueryException query exception
   */
  public static Integer[] sort(final ValueList vl, final StandardFunc sf) throws QueryException {
    final int al = vl.size();
    final Integer[] order = new Integer[al];
    for(int o = 0; o < al; o++) order[o] = o;
    try {
      Arrays.sort(order, new Comparator<Integer>() {
        @Override
        public int compare(final Integer i1, final Integer i2) {
          try {
            final Value v1 = vl.get(i1), v2 = vl.get(i2);
            final long s1 = v1.size(), s2 = v2.size(), sl = Math.min(s1, s2);
            for(int v = 0; v < sl; v++) {
              Item m = v1.itemAt(v), n = v2.itemAt(v);
              if(m == Dbl.NAN || m == Flt.NAN) m = null;
              if(n == Dbl.NAN || n == Flt.NAN) n = null;
              if(m != null && n != null && !m.comparable(n)) {
                throw m instanceof FItem ? FIEQ_X.get(sf.info, m.type) :
                      n instanceof FItem ? FIEQ_X.get(sf.info, n.type) :
                      diffError(m, n, sf.info);
              }
              final int d = m == null ? n == null ? 0 : -1 : n == null ? 1 :
                m.diff(n, sf.sc.collation, sf.info);
              if(d != 0 && d != Item.UNDEF) return d;
            }
            return (int) (s1 - s2);
          } catch(final QueryException ex) {
            throw new QueryRTException(ex);
          }
        }
      });
    } catch(final QueryRTException ex) {
      throw ex.getCause();
    }
    return order;
  }
}
