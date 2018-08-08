package org.basex.query.func.zip;

import static org.basex.query.QueryError.SAXERR_X;

import java.io.IOException;

import org.basex.build.Parser;
import org.basex.build.html.HtmlParser;
import org.basex.core.MainOptions;
import org.basex.io.IO;
import org.basex.io.IOContent;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.ANode;
import org.basex.query.value.node.DBNode;
import org.basex.util.InputInfo;

/**
 * Functions on zip files.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class ZipXmlEntry extends ZipBinaryEntry {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return xmlEntry(qc, false);
  }

  /**
   * Returns a document node, created from an XML or HTML file.
   * @param qc query context
   * @param html html flag
   * @return binary result
   * @throws QueryException query exception
   */
  final ANode xmlEntry(final QueryContext qc, final boolean html) throws QueryException {
    final MainOptions opts = qc.context.options;
    final IO io = new IOContent(entry(qc));
    try {
      return new DBNode(html ? new HtmlParser(io, opts) : Parser.xmlParser(io));
    } catch(final IOException ex) {
      throw SAXERR_X.get(info, ex);
    }
  }
}
