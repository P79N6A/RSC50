package org.basex.query.func.fn;

import static org.basex.query.QueryText.LANG;
import static org.basex.util.Token.eq;
import static org.basex.util.Token.lc;
import static org.basex.util.Token.normalize;
import static org.basex.util.Token.startsWith;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.BasicNodeIter;
import org.basex.query.value.item.Bln;
import org.basex.query.value.node.ANode;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FnLang extends Ids {
  @Override
  public Bln item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] lang = lc(toEmptyToken(exprs[0], qc));
    final ANode node = toNode(ctxArg(1, qc), qc);
    for(ANode n = node; n != null; n = n.parent()) {
      final BasicNodeIter atts = n.attributes();
      for(ANode at; (at = atts.next()) != null;) {
        if(eq(at.qname().string(), LANG)) {
          final byte[] ln = lc(normalize(at.string()));
          return Bln.get(startsWith(ln, lang) &&
              (lang.length == ln.length || ln[lang.length] == '-'));
        }
      }
    }
    return Bln.FALSE;
  }
}
