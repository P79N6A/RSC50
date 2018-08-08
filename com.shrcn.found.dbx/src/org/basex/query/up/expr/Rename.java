package org.basex.query.up.expr;

import static org.basex.query.QueryError.UPNSCONFL_X_X;
import static org.basex.query.QueryError.UPSEQEMP_X;
import static org.basex.query.QueryError.UPWRTRGSINGLE_X;
import static org.basex.query.QueryError.UPWRTRGTYP_X;
import static org.basex.query.QueryText.AS;
import static org.basex.query.QueryText.NODE;
import static org.basex.query.QueryText.RENAME;
import static org.basex.util.Token.eq;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.StaticContext;
import org.basex.query.expr.Expr;
import org.basex.query.expr.constr.CAttr;
import org.basex.query.expr.constr.CElem;
import org.basex.query.expr.constr.CNode;
import org.basex.query.expr.constr.CPI;
import org.basex.query.iter.Iter;
import org.basex.query.up.Updates;
import org.basex.query.up.primitives.node.RenameNode;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.QNm;
import org.basex.query.value.node.ANode;
import org.basex.query.value.node.DBNode;
import org.basex.query.value.seq.Empty;
import org.basex.query.value.type.NodeType;
import org.basex.query.var.Var;
import org.basex.util.Atts;
import org.basex.util.InputInfo;
import org.basex.util.Util;
import org.basex.util.hash.IntObjMap;

/**
 * Rename expression.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Lukas Kircher
 */
public final class Rename extends Update {
  /**
   * Constructor.
   * @param sc static context
   * @param info input info
   * @param trg target expression
   * @param name new name expression
   */
  public Rename(final StaticContext sc, final InputInfo info, final Expr trg, final Expr name) {
    super(sc, info, trg, name);
  }

  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Iter t = qc.iter(exprs[0]);
    final Item i = t.next();

    // check target constraints
    if(i == null) throw UPSEQEMP_X.get(info, Util.className(this));
    final Item i2 = t.next();
    if(i2 != null) throw UPWRTRGSINGLE_X.get(info, ValueBuilder.concat(i, i2));

    final CNode ex;
    if(i.type == NodeType.ELM) {
      ex = new CElem(sc, info, exprs[1], null);
    } else if(i.type == NodeType.ATT) {
      ex = new CAttr(sc, info, false, exprs[1], Empty.SEQ);
    } else if(i.type == NodeType.PI) {
      ex = new CPI(sc, info, exprs[1], Empty.SEQ);
    } else {
      throw UPWRTRGTYP_X.get(info, i);
    }

    final QNm rename = ex.item(qc, info).qname();
    final ANode targ = (ANode) i;

    // check namespace conflicts...
    if(targ.type == NodeType.ELM || targ.type == NodeType.ATT) {
      final byte[] rp = rename.prefix();
      final byte[] ru = rename.uri();
      final Atts at = targ.nsScope(sc);
      final int as = at.size();
      for(int a = 0; a < as; a++) {
        if(eq(at.name(a), rp) && !eq(at.value(a), ru))
          throw UPNSCONFL_X_X.get(info, rename, new QNm(at.name(a), at.value(a)));
      }
    }

    final Updates updates = qc.updates();
    final DBNode dbn = updates.determineDataRef(targ, qc);
    updates.add(new RenameNode(dbn.pre(), dbn.data(), info, rename), qc);
    return null;
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new Rename(sc, info, exprs[0].copy(cc, vm), exprs[1].copy(cc, vm));
  }

  @Override
  public String toString() {
    return RENAME + ' ' + NODE + ' ' + exprs[0] + ' ' + AS + ' ' + exprs[1];
  }
}
