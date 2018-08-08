package org.basex.query.up.expr;

import static org.basex.query.QueryError.UPATTDUPL_X;
import static org.basex.query.QueryError.UPATTELM2_X;
import static org.basex.query.QueryError.UPATTELM_X;
import static org.basex.query.QueryError.UPNOATTRPER_X;
import static org.basex.query.QueryError.UPPAREMPTY_X;
import static org.basex.query.QueryError.UPSEQEMP_X;
import static org.basex.query.QueryError.UPTRGSNGL2_X;
import static org.basex.query.QueryError.UPTRGSNGL_X;
import static org.basex.query.QueryError.UPTRGTYP2_X;
import static org.basex.query.QueryError.UPTRGTYP_X;
import static org.basex.query.QueryText.INSERT;
import static org.basex.query.QueryText.INTO;
import static org.basex.query.QueryText.NODE;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.StaticContext;
import org.basex.query.expr.Expr;
import org.basex.query.expr.constr.Constr;
import org.basex.query.iter.Iter;
import org.basex.query.up.Updates;
import org.basex.query.up.primitives.node.InsertAfter;
import org.basex.query.up.primitives.node.InsertAttribute;
import org.basex.query.up.primitives.node.InsertBefore;
import org.basex.query.up.primitives.node.InsertInto;
import org.basex.query.up.primitives.node.InsertIntoAsFirst;
import org.basex.query.up.primitives.node.InsertIntoAsLast;
import org.basex.query.up.primitives.node.NodeUpdate;
import org.basex.query.util.list.ANodeList;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.ANode;
import org.basex.query.value.node.DBNode;
import org.basex.query.value.type.NodeType;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.Util;
import org.basex.util.hash.IntObjMap;

/**
 * Insert expression.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Lukas Kircher
 */
public final class Insert extends Update {
  /** First flag. */
  private final boolean first;
  /** Last flag. */
  private final boolean last;
  /** Before flag. */
  private final boolean before;
  /** After flag. */
  private final boolean after;

  /**
   * Constructor.
   * @param sc static context
   * @param info input info
   * @param src source expression
   * @param first first flag
   * @param last last
   * @param before before
   * @param after after
   * @param trg target expression
   */
  public Insert(final StaticContext sc, final InputInfo info, final Expr src, final boolean first,
      final boolean last, final boolean before, final boolean after, final Expr trg) {
    super(sc, info, trg, src);
    this.first = first;
    this.last = last;
    this.before = before;
    this.after = after;
  }

  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Constr c = new Constr(info, sc).add(qc, exprs[1]);
    final ANodeList cList = c.children, aList = c.atts;
    if(c.errAtt != null) throw UPNOATTRPER_X.get(info, c.errAtt);
    if(c.duplAtt != null) throw UPATTDUPL_X.get(info, c.duplAtt);

    // check target constraints
    final Iter t = qc.iter(exprs[0]);
    final Item i = t.next();
    if(i == null) throw UPSEQEMP_X.get(info, Util.className(this));

    final boolean loc = before || after;
    if(!(i instanceof ANode)) throw (loc ? UPTRGTYP2_X : UPTRGTYP_X).get(info, i);
    final Item i2 = t.next();
    if(i2 != null) throw (loc ? UPTRGSNGL2_X : UPTRGSNGL_X).get(info, ValueBuilder.concat(i, i2));

    final ANode n = (ANode) i;
    final ANode par = n.parent();
    if(loc) {
      if(n.type == NodeType.ATT || n.type == NodeType.DOC) throw UPTRGTYP2_X.get(info, n);
      if(par == null) throw UPPAREMPTY_X.get(info, n);
    } else {
      if(n.type != NodeType.ELM && n.type != NodeType.DOC) throw UPTRGTYP_X.get(info, n);
    }

    NodeUpdate up;
    DBNode dbn;
    // no update primitive is created if node list is empty
    final Updates updates = qc.updates();
    if(!aList.isEmpty()) {
      final ANode targ = loc ? par : n;
      if(targ.type != NodeType.ELM) throw (loc ? UPATTELM_X : UPATTELM2_X).get(info, targ);

      dbn = updates.determineDataRef(targ, qc);
      up = new InsertAttribute(dbn.pre(), dbn.data(), info, checkNS(aList, targ));
      updates.add(up, qc);
    }

    // no update primitive is created if node list is empty
    if(!cList.isEmpty()) {
      dbn = updates.determineDataRef(n, qc);
      if(before) up = new InsertBefore(dbn.pre(), dbn.data(), info, cList);
      else if(after) up = new InsertAfter(dbn.pre(), dbn.data(), info, cList);
      else if(first) up = new InsertIntoAsFirst(dbn.pre(), dbn.data(), info, cList);
      else if(last) up = new InsertIntoAsLast(dbn.pre(), dbn.data(), info, cList);
      else up = new InsertInto(dbn.pre(), dbn.data(), info, cList);
      updates.add(up, qc);
    }
    return null;
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new Insert(sc, info, exprs[1].copy(cc, vm), first, last, before, after,
        exprs[0].copy(cc, vm));
  }

  @Override
  public String toString() {
    return INSERT + ' ' + NODE + ' ' + exprs[1] + ' ' + INTO + ' ' + exprs[0];
  }
}
