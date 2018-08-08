package org.basex.query.expr.ft;

import static org.basex.query.QueryText.AT;
import static org.basex.query.QueryText.CONTENT;
import static org.basex.query.QueryText.END;
import static org.basex.query.QueryText.ENTIRE;
import static org.basex.query.QueryText.START;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.util.ft.FTMatch;
import org.basex.query.util.ft.FTStringMatch;
import org.basex.query.value.node.FElem;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.ft.FTContents;
import org.basex.util.ft.FTLexer;
import org.basex.util.hash.IntObjMap;

/**
 * FTContent expression.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FTContent extends FTFilter {
  /** Content type. */
  private final FTContents content;

  /**
   * Constructor.
   * @param info input info
   * @param expr expression
   * @param content contents type
   */
  public FTContent(final InputInfo info, final FTExpr expr, final FTContents content) {
    super(info, expr);
    this.content = content;
  }

  @Override
  protected boolean filter(final QueryContext qc, final FTMatch match, final FTLexer lexer) {
    if(content == FTContents.START) {
      for(final FTStringMatch sm : match) if(sm.start == 0) return true;
    } else if(content == FTContents.END) {
      final int p = lexer.count() - 1;
      for(final FTStringMatch sm : match) if(sm.end == p) return true;
    } else {
      final int s = lexer.count();
      final boolean[] bl = new boolean[s];
      for(final FTStringMatch sm : match) {
        if(sm.gaps) continue;
        for(int p = sm.start; p <= sm.end; ++p) bl[p] = true;
      }
      for(final boolean b : bl) if(!b) return false;
      return true;
    }
    return false;
  }

  @Override
  protected boolean content() {
    return content != FTContents.START;
  }

  @Override
  public FTExpr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new FTContent(info, exprs[0].copy(cc, vm), content);
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(CONTENT, content.toString()), exprs);
  }

  @Override
  public String toString() {
    return super.toString() + (
      content == FTContents.START ? AT + ' ' + START :
      content == FTContents.END   ? AT + ' ' + END :
        ENTIRE + ' ' + CONTENT);
  }
}
