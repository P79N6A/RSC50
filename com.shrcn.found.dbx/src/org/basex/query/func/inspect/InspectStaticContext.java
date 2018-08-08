package org.basex.query.func.inspect;

import static org.basex.query.QueryError.INSPECT_UNKNOWN_X;
import static org.basex.query.QueryError.INVFUNCITEM_X_X;
import static org.basex.query.QueryText.BASE_URI;
import static org.basex.query.QueryText.BOUNDARY_SPACE;
import static org.basex.query.QueryText.COLLATION;
import static org.basex.query.QueryText.CONSTRUCTION;
import static org.basex.query.QueryText.COPY_NAMESPACES;
import static org.basex.query.QueryText.DECIMAL_FORMATS;
import static org.basex.query.QueryText.DEFAULT_ORDER_EMPTY;
import static org.basex.query.QueryText.DF_DEC;
import static org.basex.query.QueryText.DF_DIG;
import static org.basex.query.QueryText.DF_EXP;
import static org.basex.query.QueryText.DF_GRP;
import static org.basex.query.QueryText.DF_INF;
import static org.basex.query.QueryText.DF_MIN;
import static org.basex.query.QueryText.DF_NAN;
import static org.basex.query.QueryText.DF_PAT;
import static org.basex.query.QueryText.DF_PC;
import static org.basex.query.QueryText.DF_PM;
import static org.basex.query.QueryText.DF_ZD;
import static org.basex.query.QueryText.ELEMENT_NAMESPACE;
import static org.basex.query.QueryText.FUNCTION_NAMESPACE;
import static org.basex.query.QueryText.GREATEST;
import static org.basex.query.QueryText.INHERIT;
import static org.basex.query.QueryText.LEAST;
import static org.basex.query.QueryText.NAMESPACES;
import static org.basex.query.QueryText.NO_INHERIT;
import static org.basex.query.QueryText.NO_PRESERVE;
import static org.basex.query.QueryText.ORDERED;
import static org.basex.query.QueryText.ORDERING;
import static org.basex.query.QueryText.PRESERVE;
import static org.basex.query.QueryText.STRIP;
import static org.basex.query.QueryText.UNORDERED;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryText;
import org.basex.query.StaticContext;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.ValueIter;
import org.basex.query.util.NSGlobal;
import org.basex.query.util.format.DecFormatter;
import org.basex.query.value.Value;
import org.basex.query.value.item.FuncItem;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.query.value.item.Uri;
import org.basex.query.value.map.Map;
import org.basex.query.value.seq.Empty;
import org.basex.query.value.seq.StrSeq;
import org.basex.util.Atts;
import org.basex.util.Token;
import org.basex.util.TokenBuilder;
import org.basex.util.list.TokenList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class InspectStaticContext extends StandardFunc {
  @Override
  public Value value(final QueryContext qc) throws QueryException {
    Item it = exprs[0].item(qc, info);
    final String name = Token.string(toToken(exprs[1], qc));
    final StaticContext sctx;
    if(it == null) {
      sctx = sc;
    } else {
      it = toFunc(it, qc);
      if(!(it instanceof FuncItem)) throw INVFUNCITEM_X_X.get(info, it.type, it);
      sctx = ((FuncItem) it).sc;
    }

    switch(name) {
      case BASE_URI:
        return sctx.baseURI();
      case NAMESPACES:
        Map map = Map.EMPTY;
        Atts nsp = sctx.ns.ns;
        int ns = nsp.size();
        for(int n = 0; n < ns; n++) {
          map = map.put(Str.get(nsp.name(n)), Str.get(nsp.value(n)), info);
        }
        nsp = NSGlobal.NS;
        ns = nsp.size();
        for(int n = 0; n < ns; n++) {
          final Str key = Str.get(nsp.name(n));
          if(!map.contains(key, info)) map = map.put(key, Str.get(nsp.value(n)), info);
        }
        return map;
      case ELEMENT_NAMESPACE:
        return sctx.elemNS == null ? Empty.SEQ : Uri.uri(sctx.elemNS);
      case FUNCTION_NAMESPACE:
        return sctx.funcNS == null ? Empty.SEQ : Uri.uri(sctx.funcNS);
      case COLLATION:
        return Uri.uri(sctx.collation == null ? QueryText.COLLATION_URI : sctx.collation.uri());
      case ORDERING:
        return Str.get(sctx.ordered ? ORDERED : UNORDERED);
      case CONSTRUCTION:
        return Str.get(sctx.strip ? STRIP : PRESERVE);
      case DEFAULT_ORDER_EMPTY:
        return Str.get(sctx.orderGreatest ? GREATEST : LEAST);
      case BOUNDARY_SPACE:
        return Str.get(sctx.spaces ? PRESERVE : STRIP);
      case COPY_NAMESPACES:
        final TokenList sl = new TokenList(2);
        sl.add(sctx.preserveNS ? PRESERVE : NO_PRESERVE);
        sl.add(sctx.inheritNS ? INHERIT : NO_INHERIT);
        return StrSeq.get(sl);
      case DECIMAL_FORMATS:
        map = Map.EMPTY;
        // enforce creation of default formatter
        sctx.decFormat(Token.EMPTY);
        // loop through all formatters
        for(final byte[] format : sctx.decFormats) {
          final DecFormatter df = sctx.decFormats.get(format);
          map = map.put(Str.get(format), Map.EMPTY.
              put(Str.get(DF_DEC), Str.get(token(df.decimal)), info).
              put(Str.get(DF_EXP), Str.get(token(df.exponent)), info).
              put(Str.get(DF_GRP), Str.get(token(df.grouping)), info).
              put(Str.get(DF_PC), Str.get(token(df.percent)), info).
              put(Str.get(DF_PM), Str.get(token(df.permille)), info).
              put(Str.get(DF_ZD), Str.get(token(df.zero)), info).
              put(Str.get(DF_DIG), Str.get(token(df.optional)), info).
              put(Str.get(DF_PAT), Str.get(token(df.pattern)), info).
              put(Str.get(DF_INF), Str.get(df.inf), info).
              put(Str.get(DF_NAN), Str.get(df.nan), info).
              put(Str.get(DF_MIN), Str.get(token(df.minus)), info)
          , info);
        }
        return map;
      default:
        throw INSPECT_UNKNOWN_X.get(info, name);
    }
  }

  @Override
  public ValueIter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  /**
   * Converts codepoints to a token.
   * @param cps codepoints
   * @return token
   */
  private static byte[] token(final int... cps) {
    final TokenBuilder tb = new TokenBuilder(cps.length);
    for(final int cp : cps) tb.add(cp);
    return tb.finish();
  }
}
