package org.basex.query.func.ft;

import org.basex.data.Data;
import org.basex.index.IndexType;
import org.basex.index.query.IndexEntries;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.func.index.IndexFn;
import org.basex.query.iter.Iter;
import org.basex.query.util.ASTVisitor;
import org.basex.util.Token;
import org.basex.util.ft.FTLexer;
import org.basex.util.ft.FTOpt;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FtTokens extends StandardFunc {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final Data data = checkData(qc);
    byte[] entry = exprs.length < 2 ? Token.EMPTY : toToken(exprs[1], qc);
    if(entry.length != 0) {
      final FTLexer lexer = new FTLexer(new FTOpt().assign(data.meta));
      lexer.init(entry);
      entry = lexer.nextToken();
    }
    return IndexFn.entries(data, new IndexEntries(entry, IndexType.FULLTEXT), this);
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return dataLock(visitor, 0) && super.accept(visitor);
  }
}
