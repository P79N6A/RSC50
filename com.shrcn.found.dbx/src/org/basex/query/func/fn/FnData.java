package org.basex.query.func.fn;

import org.basex.core.locks.DBLocking;
import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.util.ASTVisitor;
import org.basex.query.value.Value;
import org.basex.query.value.type.AtomType;
import org.basex.query.value.type.NodeType;
import org.basex.query.value.type.SeqType;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnData extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return ctxArg(0, qc).atomIter(qc, info);
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    return ctxArg(0, qc).atomValue(qc, info);
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    final Expr expr = exprs.length > 0 ? exprs[0] : cc.qc.value != null ? cc.qc.value : this;
    final SeqType st = expr.seqType();
    if(st.type instanceof NodeType) {
      seqType = SeqType.get(AtomType.ATM, st.occ);
    } else if(!st.mayBeArray()) {
      seqType = st;
    }
    return this;
  }

  @Override
  public boolean has(final Flag flag) {
    return flag == Flag.CTX && exprs.length == 0 || super.has(flag);
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return (exprs.length != 0 || visitor.lock(DBLocking.CONTEXT)) && super.accept(visitor);
  }
}
