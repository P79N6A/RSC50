package org.basex.query.func.ft;

import static org.basex.query.QueryError.BXDB_INDEX_X;
import static org.basex.query.QueryError.BXFT_MATCH;
import static org.basex.util.ft.FTFlag.FZ;
import static org.basex.util.ft.FTFlag.WC;

import org.basex.data.Data;
import org.basex.index.IndexType;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.ft.FTIndexAccess;
import org.basex.query.expr.ft.FTWords;
import org.basex.query.iter.Iter;
import org.basex.query.util.ASTVisitor;
import org.basex.query.util.IndexContext;
import org.basex.query.value.Value;
import org.basex.util.ft.FTMode;
import org.basex.util.ft.FTOpt;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FtSearch extends FtAccess {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final Data data = checkData(qc);
    final Value terms = qc.value(exprs[1]);
    final FtIndexOptions opts = toOptions(2, new FtIndexOptions(), qc);

    final IndexContext ic = new IndexContext(data, false);
    if(!data.meta.ftindex) throw BXDB_INDEX_X.get(info, data.meta.name, IndexType.FULLTEXT);

    final FTOpt opt = new FTOpt().assign(data.meta);
    final FTMode mode = opts.get(FtIndexOptions.MODE);
    opt.set(FZ, opts.get(FtIndexOptions.FUZZY));
    opt.set(WC, opts.get(FtIndexOptions.WILDCARDS));
    if(opt.is(FZ) && opt.is(WC)) throw BXFT_MATCH.get(info, this);

    final FTWords ftw = new FTWords(info, data, terms, mode).init(qc, opt);
    return new FTIndexAccess(info, options(ftw, opts), ic).iter(qc);
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return dataLock(visitor, 0) && super.accept(visitor);
  }
}
