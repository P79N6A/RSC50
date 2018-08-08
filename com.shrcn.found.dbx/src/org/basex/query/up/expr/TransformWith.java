package org.basex.query.up.expr;

import static org.basex.query.QueryError.BASX_UPMODIFY;
import static org.basex.query.QueryError.UPMODIFY;
import static org.basex.query.QueryError.UPSOURCE_X;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryText;
import org.basex.query.expr.Arr;
import org.basex.query.expr.Expr;
import org.basex.query.iter.Iter;
import org.basex.query.iter.ValueIter;
import org.basex.query.up.Updates;
import org.basex.query.value.Value;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.ANode;
import org.basex.query.value.node.FElem;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * Transform expression.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class TransformWith extends Arr {
  /**
   * Constructor.
   * @param info input info
   * @param source source expression
   * @param modify modify expression
   */
  public TransformWith(final InputInfo info, final Expr source, final Expr modify) {
    super(info, source, modify);
  }

  @Override
  public Expr compile(final CompileContext cc) throws QueryException {
    final Value v = cc.qc.value;
    try {
      cc.qc.value = null;
      return super.compile(cc);
    } finally {
      cc.qc.value = v;
    }
  }

  @Override
  public void checkUp() throws QueryException {
    checkNoUp(exprs[0]);
    final Expr modify = exprs[1];
    modify.checkUp();
    if(!modify.isVacuous() && !modify.has(Flag.UPD)) throw UPMODIFY.get(info);
  }

  @Override
  public ValueIter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final Updates upd = qc.updates();
    final Value cv = qc.value;

    final ValueBuilder vb = new ValueBuilder();
    try {
      final Iter ir = qc.iter(exprs[0]);
      for(Item it; (it = ir.next()) != null;) {
        if(!(it instanceof ANode)) throw UPSOURCE_X.get(info, it);

        // copy node to main memory data instance
        it = ((ANode) it).dbNodeCopy(qc.context.options);
        // set resulting node as context
        qc.value = it;

        final Updates updates = new Updates(true);
        qc.updates = updates;
        updates.addData(it.data());

        final Value v = qc.value(exprs[1]);
        if(!v.isEmpty()) throw BASX_UPMODIFY.get(info);

        updates.prepare(qc);
        updates.apply(qc);
        vb.add(it);
      }
    } finally {
      qc.updates = upd;
      qc.value = cv;
    }
    return vb.value();
  }

  @Override
  public boolean has(final Flag flag) {
    return flag == Flag.UPD ? exprs[0].has(flag) : super.has(flag);
    //return flag != Flag.UPD && super.has(flag);
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new TransformWith(info, exprs[0].copy(cc, vm), exprs[1].copy(cc, vm));
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(), exprs);
  }

  @Override
  public String toString() {
    return toString(' ' + QueryText.UPDATE + ' ');
  }

  @Override
  public int exprSize() {
    int sz = 1;
    for(final Expr expr : exprs) sz += expr.exprSize();
    return sz;
  }
}
