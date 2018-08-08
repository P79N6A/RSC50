package org.basex.query.expr.constr;

import static org.basex.query.QueryText.COMMENT;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.StaticContext;
import org.basex.query.expr.Expr;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.FComm;
import org.basex.query.value.type.SeqType;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.TokenBuilder;
import org.basex.util.hash.IntObjMap;

/**
 * Comment fragment.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class CComm extends CNode {
  /**
   * Constructor.
   * @param sc static context
   * @param info input info
   * @param comment comment
   */
  public CComm(final StaticContext sc, final InputInfo info, final Expr comment) {
    super(sc, info, comment);
    seqType = SeqType.COM;
  }

  @Override
  public FComm item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Iter iter = exprs[0].atomIter(qc, info);

    final TokenBuilder tb = new TokenBuilder();
    boolean more = false;
    for(Item it; (it = iter.next()) != null;) {
      if(more) tb.add(' ');
      tb.add(it.string(info));
      more = true;
    }
    return new FComm(FComm.parse(tb.finish(), info));
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new CComm(sc, info, exprs[0].copy(cc, vm));
  }

  @Override
  public String description() {
    return info(COMMENT);
  }

  @Override
  public String toString() {
    return toString(COMMENT);
  }
}
