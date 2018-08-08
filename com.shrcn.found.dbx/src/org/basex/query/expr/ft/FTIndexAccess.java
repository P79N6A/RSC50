package org.basex.query.expr.ft;

import static org.basex.query.QueryText.DATA;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.expr.Simple;
import org.basex.query.func.Function;
import org.basex.query.iter.FTIter;
import org.basex.query.iter.NodeIter;
import org.basex.query.util.ASTVisitor;
import org.basex.query.util.IndexContext;
import org.basex.query.value.item.Str;
import org.basex.query.value.node.ANode;
import org.basex.query.value.node.FElem;
import org.basex.query.value.node.FTNode;
import org.basex.query.var.Var;
import org.basex.query.var.VarUsage;
import org.basex.util.InputInfo;
import org.basex.util.ft.FTMode;
import org.basex.util.hash.IntObjMap;

/**
 * FTContains expression with index access.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FTIndexAccess extends Simple {
  /** Full-text expression. */
  private final FTExpr ftexpr;
  /** Database name. */
  private final IndexContext ictx;

  /**
   * Constructor.
   * @param info input info
   * @param ftexpr contains, select and optional ignore expression
   * @param ictx index context
   */
  public FTIndexAccess(final InputInfo info, final FTExpr ftexpr, final IndexContext ictx) {
    super(info);
    this.ftexpr = ftexpr;
    this.ictx = ictx;
  }

  @Override
  public NodeIter iter(final QueryContext qc) throws QueryException {
    final FTIter ir = ftexpr.iter(qc);

    return new NodeIter() {
      @Override
      public ANode next() throws QueryException {
        final FTNode it = ir.next();
        if(it != null) {
          // assign scoring
          if(qc.scoring) it.score();
          // cache entry for visualizations or ft:mark/ft:extract
          if(qc.ftPosData != null) qc.ftPosData.add(it.data(), it.pre(), it.matches());
          // remove matches reference to save memory
          it.matches(null);
        }
        return it;
      }
    };
  }

  @Override
  public boolean has(final Flag flag) {
    return ftexpr.has(flag);
  }

  @Override
  public boolean removable(final Var var) {
    return ftexpr.removable(var);
  }

  @Override
  public VarUsage count(final Var var) {
    return ftexpr.count(var);
  }

  @Override
  public Expr inline(final Var var, final Expr ex, final CompileContext cc) throws QueryException {
    return ftexpr.inline(var, ex, cc) == null ? null : optimize(cc);
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new FTIndexAccess(info, ftexpr.copy(cc, vm), ictx);
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return visitor.lock(ictx.data.meta.name) && ftexpr.accept(visitor);
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(DATA, ictx.data.meta.name), ftexpr);
  }

  @Override
  public boolean iterable() {
    return ictx.iterable;
  }

  @Override
  public int exprSize() {
    return ftexpr.exprSize() + 1;
  }

  @Override
  public String toString() {
    Expr e = ftexpr;
    if(ftexpr instanceof FTWords) {
      final FTWords ftw = (FTWords) ftexpr;
      if(ftw.mode == FTMode.ANY && ftw.occ == null && ftw.simple) e = ftw.query;
    }
    return Function._FT_SEARCH.toString(Str.get(ictx.data.meta.name), e);
  }
}
