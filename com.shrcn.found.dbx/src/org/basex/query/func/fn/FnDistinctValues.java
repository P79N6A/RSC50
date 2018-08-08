package org.basex.query.func.fn;

import java.util.ArrayList;

import org.basex.data.Data;
import org.basex.index.path.PathNode;
import org.basex.index.stats.StatsType;
import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.expr.path.AxisPath;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.util.collation.Collation;
import org.basex.query.util.collation.CollationItemSet;
import org.basex.query.util.hash.HashItemSet;
import org.basex.query.util.hash.ItemSet;
import org.basex.query.value.Value;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.Atm;
import org.basex.query.value.item.Item;
import org.basex.query.value.seq.RangeSeq;
import org.basex.query.value.type.AtomType;
import org.basex.query.value.type.NodeType;
import org.basex.query.value.type.SeqType;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnDistinctValues extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final Collation coll = toCollation(1, qc);
    if(exprs[0] instanceof RangeSeq) return exprs[0].iter(qc);

    return new Iter() {
      final ItemSet set = coll == null ? new HashItemSet() : new CollationItemSet(coll);
      final Iter ir = exprs[0].atomIter(qc, info);

      @Override
      public Item next() throws QueryException {
        while(true) {
          final Item it = ir.next();
          if(it == null) return null;
          if(set.add(it, info)) return it;
        }
      }
    };
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final Collation coll = toCollation(1, qc);
    if(exprs[0] instanceof RangeSeq) return (RangeSeq) exprs[0];

    final ValueBuilder vb = new ValueBuilder();
    final ItemSet set = coll == null ? new HashItemSet() : new CollationItemSet(coll);
    final Iter ir = exprs[0].atomIter(qc, info);
    for(Item it; (it = ir.next()) != null;) if(set.add(it, info)) vb.add(it);
    return vb.value();
  }

  @Override
  protected Expr opt(final CompileContext cc) throws QueryException {
    final SeqType st = exprs[0].seqType();
    if(st.type instanceof NodeType) {
      seqType = SeqType.get(AtomType.ATM, st.occ);
    } else if(!st.mayBeArray()) {
      seqType = st;
    }
    return exprs.length == 1 ? cmpDist(cc) : this;
  }

  /**
   * Pre-evaluates distinct-values() function, utilizing database statistics.
   * @param cc compilation context
   * @return original or optimized expression
   * @throws QueryException query exception
   */
  private Expr cmpDist(final CompileContext cc) throws QueryException {
    // can only be performed on axis paths
    if(!(exprs[0] instanceof AxisPath)) return this;

    // try to get statistics for resulting nodes
    final ArrayList<PathNode> nodes = ((AxisPath) exprs[0]).pathNodes(cc);
    if(nodes == null) return this;
    // loop through all nodes
    final HashItemSet is = new HashItemSet();
    for(PathNode pn : nodes) {
      // retrieve text child if addressed node is an element
      if(pn.kind == Data.ELEM) {
        if(!pn.stats.isLeaf()) return this;
        for(final PathNode n : pn.children) if(n.kind == Data.TEXT) pn = n;
      }
      // skip nodes others than texts and attributes
      if(pn.kind != Data.TEXT && pn.kind != Data.ATTR) return this;
      // check if distinct values are available
      if(pn.stats.type != StatsType.CATEGORY) return this;
      // if yes, add them to the item set
      for(final byte[] c : pn.stats.cats) is.put(new Atm(c), info);
    }
    // return resulting sequence
    final ValueBuilder vb = new ValueBuilder();
    for(final Item i : is) vb.add(i);
    return vb.value();
  }
}
