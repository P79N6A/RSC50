package org.basex.query.expr.ft;

import static org.basex.query.QueryError.FTWILDCARD_X;
import static org.basex.util.Token.eq;
import static org.basex.util.ft.FTFlag.FZ;
import static org.basex.util.ft.FTFlag.WC;

import org.basex.core.MainOptions;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.util.ft.FTMatches;
import org.basex.util.InputInfo;
import org.basex.util.ft.FTBitapSearch.TokenComparator;
import org.basex.util.ft.FTIterator;
import org.basex.util.ft.FTLexer;
import org.basex.util.ft.FTOpt;
import org.basex.util.hash.TokenObjMap;
import org.basex.util.list.TokenList;
import org.basex.util.similarity.Levenshtein;

/**
 * This class performs the full-text tokenization.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class FTTokenizer {
  /** Token comparator. */
  final TokenComparator cmp;

  /** Wildcard object cache. */
  private final TokenObjMap<FTWildcard> wcCache = new TokenObjMap<>();
  /** Token cache. */
  private final TokenObjMap<FTTokens> cache = new TokenObjMap<>();
  /** Levenshtein reference. */
  private final Levenshtein ls;
  /** Input info. */
  private final InputInfo info;
  /** Full-text options. */
  private final FTOpt opt;

  /** All matches. */
  FTMatches matches = new FTMatches();
  /** Flag for first evaluation. */
  boolean first;
  /** Query position. */
  int pos;

  /**
   * Constructor.
   * @param opt full-text options
   * @param qc query context
   * @param info input info
   */
  FTTokenizer(final FTOpt opt, final QueryContext qc, final InputInfo info) {
    this(opt, new Levenshtein(qc.context.options.get(MainOptions.LSERROR)), info);
  }

  /**
   * Constructor.
   * @param opt full-text options
   * @param ls Levenshtein distance calculation
   * @param info input info
   */
  private FTTokenizer(final FTOpt opt, final Levenshtein ls, final InputInfo info) {
    this.opt = opt;
    this.ls = ls;
    this.info = info;

    cmp = new TokenComparator() {
      @Override
      public boolean equal(final byte[] in, final byte[] qu) throws QueryException {
        FTWildcard ftw = null;
        if(opt.is(WC)) {
          ftw = wcCache.get(qu);
          if(ftw == null) {
            ftw = new FTWildcard(qu);
            if(!ftw.parse()) throw FTWILDCARD_X.get(info, qu);
            wcCache.put(qu, ftw);
          }
          // simple characters
          if(ftw.simple()) ftw = null;
        }

        return
          // skip stop words, i. e. if the current query token is a stop word,
          // it is always equal to the corresponding input token:
          opt.sw != null && opt.sw.contains(qu) ||
          // fuzzy search:
          (opt.is(FZ) ? ls.similar(in, qu) :
          // wild-card search:
          ftw != null ? ftw.match(in) :
          // simple search:
          eq(in, qu));
      }
    };
  }

  /**
   * Returns cached query tokens.
   * @param query query token
   * @return number of occurrences
   * @throws QueryException query exception
   */
  FTTokens cache(final byte[] query) throws QueryException {
    FTTokens tokens = cache.get(query);
    if(tokens == null) {
      tokens = new FTTokens();
      cache.put(query, tokens);

      // cache query tokens:
      final FTIterator quLex = new FTLexer(opt).init(query);
      final TokenList quList = new TokenList(1);
      while(quLex.hasNext()) quList.add(quLex.nextToken());
      tokens.add(quList);

      // if thesaurus is required, add the terms which extend the query:
      if(opt.th != null) {
        for(final byte[] ext : opt.th.find(info, query)) {
          // parse each extension term to a set of tokens:
          final TokenList tl = new TokenList(1);
          quLex.init(ext);
          while(quLex.hasNext()) tl.add(quLex.nextToken());
          // add each thesaurus term as an additional query term:
          tokens.add(tl);
        }
      }
    }
    return tokens;
  }

  /**
   * Copies this FTTokenizer.
   * @return copy
   */
  FTTokenizer copy() {
    return new FTTokenizer(opt, ls, info);
  }
}
