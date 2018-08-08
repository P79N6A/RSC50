package org.basex.query.func.ft;

import static org.basex.query.QueryError.BXFT_MATCH;
import static org.basex.util.ft.FTFlag.DC;
import static org.basex.util.ft.FTFlag.FZ;
import static org.basex.util.ft.FTFlag.ST;
import static org.basex.util.ft.FTFlag.WC;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.ft.FTContains;
import org.basex.query.expr.ft.FTWords;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;
import org.basex.util.ft.FTCase;
import org.basex.util.ft.FTDiacritics;
import org.basex.util.ft.FTMode;
import org.basex.util.ft.FTOpt;
import org.basex.util.ft.Language;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FtContains extends FtAccess {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Value input = qc.value(exprs[0]);
    final Value query = qc.value(exprs[1]);
    final FtContainsOptions opts = toOptions(2, new FtContainsOptions(), qc);

    final FTOpt opt = new FTOpt().assign(qc.ftOpt());
    final FTMode mode = opts.get(FtIndexOptions.MODE);
    opt.set(FZ, opts.get(FtIndexOptions.FUZZY));
    opt.set(WC, opts.get(FtIndexOptions.WILDCARDS));
    if(opt.is(FZ) && opt.is(WC)) throw BXFT_MATCH.get(info, this);

    final FTDiacritics dc = opts.get(FtContainsOptions.DIACRITICS);
    if(dc != null) opt.set(DC, dc == FTDiacritics.SENSITIVE);
    final Boolean st = opts.get(FtContainsOptions.STEMMING);
    if(st != null) opt.set(ST, st);
    final String ln = opts.get(FtContainsOptions.LANGUAGE);
    if(ln != null) opt.ln = Language.get(ln);
    final FTCase cs = opts.get(FtContainsOptions.CASE);
    if(cs != null) opt.cs = cs;

    final FTWords ftw = new FTWords(info, query, mode, null).init(qc, opt);
    return new FTContains(input, options(ftw, opts), info).item(qc, info);
  }
}
