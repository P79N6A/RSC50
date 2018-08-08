package org.basex.query.func.inspect;

import static org.basex.query.QueryError.IOERR_X;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.basex.io.IO;
import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.ann.Annotation;
import org.basex.query.expr.Expr;
import org.basex.query.func.Functions;
import org.basex.query.func.StandardFunc;
import org.basex.query.func.StaticFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.FuncItem;
import org.basex.util.Token;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class InspectFunctions extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    checkCreate(qc);
    // about to be updated in a future version
    final ArrayList<StaticFunc> old = new ArrayList<>();
    if(exprs.length > 0) {
      // cache existing functions
      Collections.addAll(old, qc.funcs.funcs());
      try {
        final IO io = checkPath(0, qc);
        qc.parse(Token.string(io.read()), io.path(), sc);
        qc.funcs.compile(new CompileContext(qc), true);
      } catch(final IOException ex) {
        throw IOERR_X.get(info, ex);
      }
    }

    final ValueBuilder vb = new ValueBuilder();
    for(final StaticFunc sf : qc.funcs.funcs()) {
      if(old.contains(sf)) continue;
      final FuncItem fi = Functions.getUser(sf, qc, sf.sc, info);
      if(sc.mixUpdates || !fi.annotations().contains(Annotation.UPDATING)) vb.add(fi);
    }
    return vb.value();
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    if(exprs.length == 0) cc.qc.funcs.compile(cc, true);
    return this;
  }

  @Override
  public boolean has(final Flag flag) {
    // do not relocate function, as it introduces new code
    return flag == Flag.NDT && exprs.length == 1 || super.has(flag);
  }
}
