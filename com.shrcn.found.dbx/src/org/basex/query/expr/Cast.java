package org.basex.query.expr;

import static org.basex.query.QueryError.INVCAST_X_X_X;
import static org.basex.query.QueryText.AS;
import static org.basex.query.QueryText.CAST;
import static org.basex.query.QueryText.TYP;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.StaticContext;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.FElem;
import org.basex.query.value.type.AtomType;
import org.basex.query.value.type.SeqType;
import org.basex.query.value.type.SeqType.Occ;
import org.basex.query.value.type.Type;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * Cast expression.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Cast extends Single {
  /** Static context. */
  private final StaticContext sc;

  /**
   * Function constructor.
   * @param sc static context
   * @param info input info
   * @param expr expression
   * @param seqType target type
   */
  public Cast(final StaticContext sc, final InputInfo info, final Expr expr,
      final SeqType seqType) {
    super(info, expr);
    this.sc = sc;
    this.seqType = seqType;
  }

  @Override
  public Expr compile(final CompileContext cc) throws QueryException {
    return super.compile(cc).optimize(cc);
  }

  @Override
  public Expr optimize(final CompileContext cc) throws QueryException {
    final SeqType st = expr.seqType();
    if(st.one() && !st.mayBeArray()) seqType = SeqType.get(seqType.type, Occ.ONE);

    // pre-evaluate value
    if(expr.isValue()) return optPre(value(cc.qc), cc);

    // skip cast if specified and return types are equal
    // (the following types will always be correct)
    final Type t = seqType.type;
    if((t == AtomType.BLN || t == AtomType.FLT || t == AtomType.DBL ||
        t == AtomType.QNM || t == AtomType.URI) && seqType.eq(expr.seqType())) {
      optPre(expr, cc);
      return expr;
    }
    size = seqType.occ();
    return this;
  }

  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final Value v = expr.atomValue(qc, info);
    if(!seqType.occ.check(v.size())) throw INVCAST_X_X_X.get(info, v.seqType(), seqType, v);
    return v instanceof Item ? seqType.cast((Item) v, qc, sc, info, true) : v;
  }

  @Override
  public Cast copy(final CompileContext cc, final IntObjMap<Var> vs) {
    return new Cast(sc, info, expr.copy(cc, vs), seqType);
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(TYP, seqType), expr);
  }

  @Override
  public String toString() {
    return expr + " " + CAST + ' ' + AS + ' ' + seqType;
  }
}
