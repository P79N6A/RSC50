package org.basex.query.expr.ft;

import static org.basex.query.QueryText.CONTAINS;
import static org.basex.query.QueryText.OPTINDEX_X_X;
import static org.basex.query.QueryText.TEXT;

import org.basex.index.IndexType;
import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.expr.Single;
import org.basex.query.iter.Iter;
import org.basex.query.util.ASTVisitor;
import org.basex.query.util.IndexInfo;
import org.basex.query.util.ft.FTMatches;
import org.basex.query.util.ft.FTPosData;
import org.basex.query.value.item.Bln;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.DBNode;
import org.basex.query.value.node.FElem;
import org.basex.query.value.node.FTNode;
import org.basex.query.value.type.SeqType;
import org.basex.query.var.Var;
import org.basex.query.var.VarUsage;
import org.basex.util.InputInfo;
import org.basex.util.Util;
import org.basex.util.ft.FTLexer;
import org.basex.util.ft.FTOpt;
import org.basex.util.ft.Scoring;
import org.basex.util.hash.IntObjMap;

/**
 * Abstract FTContains expression.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FTContains extends Single {
  /** Full-text expression. */
  public FTExpr ftexpr;

  /**
   * Constructor.
   * @param expr expression
   * @param ftexpr full-text expression
   * @param info input info
   */
  public FTContains(final Expr expr, final FTExpr ftexpr, final InputInfo info) {
    super(info, expr);
    this.ftexpr = ftexpr;
    seqType = SeqType.BLN;
  }

  @Override
  public Bln item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final boolean scoring = qc.scoring;
    final Iter iter = expr.iter(qc);

    final FTLexer tmp = qc.ftLexer, lexer = new FTLexer(new FTOpt());
    qc.ftLexer = lexer;
    try {
      double s = 0;
      int c = 0;
      boolean f = false;
      final FTPosData ftPosData = qc.ftPosData;
      for(Item it; (it = iter.next()) != null;) {
        lexer.init(it.string(info));
        final FTNode item = ftexpr.item(qc, info);
        final FTMatches all = item.matches();
        if(all.matches()) {
          f = true;
          if(scoring) s += item.score();
          // cache entry for visualizations or ft:mark/ft:extract
          if(ftPosData != null && it instanceof DBNode) {
            final DBNode node = (DBNode) it;
            ftPosData.add(node.data(), node.pre(), all);
          }
        }
        c++;
      }
      return scoring ? Bln.get(f, Scoring.avg(s, c)) : Bln.get(f);
    } finally {
      qc.ftLexer = tmp;
    }
  }

  @Override
  public Expr compile(final CompileContext cc) throws QueryException {
    super.compile(cc);
    ftexpr = ftexpr.compile(cc);
    return expr.isEmpty() ? optPre(Bln.FALSE, cc) : this;
  }

  @Override
  public boolean has(final Flag flag) {
    return super.has(flag) || ftexpr.has(flag);
  }

  @Override
  public boolean removable(final Var var) {
    return super.removable(var) && ftexpr.removable(var);
  }

  @Override
  public VarUsage count(final Var var) {
    return super.count(var).plus(ftexpr.count(var));
  }

  @Override
  public Expr inline(final Var var, final Expr ex, final CompileContext cc) throws QueryException {
    final Expr sub = expr.inline(var, ex, cc);
    if(sub != null) expr = sub;
    final FTExpr fte = ftexpr.inline(var, ex, cc);
    if(fte != null) ftexpr = fte;
    return sub != null || fte != null ? optimize(cc) : null;
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return super.accept(visitor) && ftexpr.accept(visitor);
  }

  @Override
  public int exprSize() {
    return super.exprSize() + ftexpr.exprSize();
  }

  @Override
  public boolean indexAccessible(final IndexInfo ii) throws QueryException {
    // check if index can be utilized
    final IndexType type = ii.type(expr, IndexType.FULLTEXT);
    if(type == null || !ftexpr.indexAccessible(ii)) return false;

    ii.create(new FTIndexAccess(info, ftexpr, ii.ic), true,
        info, Util.info(OPTINDEX_X_X, "full-text", ftexpr));
    return true;
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new FTContains(expr.copy(cc, vm), ftexpr.copy(cc, vm), info);
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(), expr, ftexpr);
  }

  @Override
  public String toString() {
    return expr + " " + CONTAINS + ' ' + TEXT + ' ' + ftexpr;
  }
}
