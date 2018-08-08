package org.basex.query.expr.constr;

import static org.basex.query.QueryText.TEXT;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.StaticContext;
import org.basex.query.expr.Expr;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.FTxt;
import org.basex.query.value.type.SeqType;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.TokenBuilder;
import org.basex.util.hash.IntObjMap;

/**
 * Text fragment.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class CTxt extends CNode {
  /**
   * Constructor.
   * @param sc static context
   * @param info input info
   * @param text text
   */
  public CTxt(final StaticContext sc, final InputInfo info, final Expr text) {
    super(sc, info, text);
    seqType = SeqType.TXT_ZO;
    size = -1;
  }

  @Override
  public Expr optimize(final CompileContext cc) {
    return optPre(oneIsEmpty() ? null : this, cc);
  }

  @Override
  public FTxt item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final TokenBuilder tb = new TokenBuilder();
    boolean more = false;

    final Iter iter = qc.iter(exprs[0]);
    for(Item it; (it = iter.next()) != null;) {
      if(more) tb.add(' ');
      tb.add(it.string(info));
      more = true;
    }
    return more ? new FTxt(tb.finish()) : null;
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new CTxt(sc, info, exprs[0].copy(cc, vm));
  }

  @Override
  public String description() {
    return info(TEXT);
  }

  @Override
  public String toString() {
    return toString(TEXT);
  }
}
