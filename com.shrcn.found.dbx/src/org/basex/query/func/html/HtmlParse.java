package org.basex.query.func.html;

import static org.basex.query.QueryError.BXHL_IO_X;

import java.io.IOException;

import org.basex.build.html.HtmlOptions;
import org.basex.core.MainOptions;
import org.basex.io.IOContent;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.DBNode;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class HtmlParse extends StandardFunc {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final byte[] in = toBytes(exprs[0], qc);
    final HtmlOptions hopts = toOptions(1, new HtmlOptions(), qc);
    final MainOptions opts = MainOptions.get();
    try {
      return new DBNode(new org.basex.build.html.HtmlParser(new IOContent(in), opts, hopts));
    } catch(final IOException ex) {
      throw BXHL_IO_X.get(info, ex);
    }
  }
}
