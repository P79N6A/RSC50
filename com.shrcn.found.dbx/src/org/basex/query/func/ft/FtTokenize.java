package org.basex.query.func.ft;

import static org.basex.util.ft.FTFlag.DC;
import static org.basex.util.ft.FTFlag.ST;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.seq.StrSeq;
import org.basex.util.ft.FTCase;
import org.basex.util.ft.FTDiacritics;
import org.basex.util.ft.FTLexer;
import org.basex.util.ft.FTOpt;
import org.basex.util.ft.Language;
import org.basex.util.list.TokenList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class FtTokenize extends FtAccess {
  @Override
  public final Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    return StrSeq.get(tokens(qc, false));
  }

  /**
   * Returns all tokens.
   * @param qc query context
   * @param all include separators
   * @return resulting tokens
   * @throws QueryException query exception
   */
  protected final TokenList tokens(final QueryContext qc, final boolean all) throws QueryException {
    final byte[] token = toToken(exprs[0], qc);
    final FtTokenizeOptions opts = toOptions(1, new FtTokenizeOptions(), qc);

    final FTOpt opt = new FTOpt().assign(qc.ftOpt());
    final FTDiacritics dc = opts.get(FtTokenizeOptions.DIACRITICS);
    if(dc != null) opt.set(DC, dc == FTDiacritics.SENSITIVE);
    final Boolean st = opts.get(FtTokenizeOptions.STEMMING);
    if(st != null) opt.set(ST, st);
    final String ln = opts.get(FtTokenizeOptions.LANGUAGE);
    if(ln != null) opt.ln = Language.get(ln);
    final FTCase cs = opts.get(FtTokenizeOptions.CASE);
    if(cs != null) opt.cs = cs;

    final TokenList tl = new TokenList();
    final FTLexer lexer = new FTLexer(opt).init(token);
    if(all) lexer.all();
    while(lexer.hasNext()) tl.add(lexer.nextToken());
    return tl;
  }
}
