package org.basex.query.expr.constr;

import static org.basex.query.QueryError.MAPDUPLKEY_X_X_X;
import static org.basex.query.QueryError.SEQFOUND_X;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryText;
import org.basex.query.expr.Arr;
import org.basex.query.expr.Expr;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.query.value.map.Map;
import org.basex.query.value.type.SeqType;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.TokenBuilder;
import org.basex.util.hash.IntObjMap;

/**
 * Map constructor.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public final class CMap extends Arr {
  /**
   * Constructor.
   * @param info input info
   * @param expr key and value expression, interleaved
   */
  public CMap(final InputInfo info, final Expr[] expr) {
    super(info, expr);
    seqType = SeqType.MAP_O;
  }

  @Override
  public Expr optimize(final CompileContext cc) throws QueryException {
    return allAreValues() ? preEval(cc) : this;
  }

  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    Map map = Map.EMPTY;
    final int es = exprs.length;
    for(int e = 0; e < es; e += 2) {
      final Value key = exprs[e].atomValue(qc, info);
      if(!(key instanceof Item)) throw SEQFOUND_X.get(info, key);
      final Item k = (Item) key;
      final Value v = qc.value(exprs[e + 1]);
      if(map.contains(k, info)) throw MAPDUPLKEY_X_X_X.get(info, k, map.get(k, info), v);
      map = map.put(k, v, info);
    }
    return map;
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new CMap(info, copyAll(cc, vm, exprs));
  }

  @Override
  public String description() {
    return QueryText.MAP;
  }

  @Override
  public String toString() {
    final TokenBuilder tb = new TokenBuilder("{ ");
    boolean key = true;
    for(final Expr expr : exprs) {
      tb.add(key ? tb.size() > 2 ? ", " : "" : ":").add(expr.toString());
      key ^= true;
    }
    return tb.add(" }").toString();
  }
}
