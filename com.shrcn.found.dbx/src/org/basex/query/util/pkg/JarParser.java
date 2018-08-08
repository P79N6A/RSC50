package org.basex.query.util.pkg;

import static org.basex.query.QueryError.BXRE_JARDESC_X;
import static org.basex.query.QueryError.BXRE_JARFAIL_X;
import static org.basex.query.util.pkg.PkgText.CLASS;
import static org.basex.query.util.pkg.PkgText.JAR;
import static org.basex.query.util.pkg.PkgText.NOCLASSES;
import static org.basex.query.util.pkg.PkgText.NOJARS;
import static org.basex.util.Token.eq;

import java.io.IOException;

import org.basex.io.IO;
import org.basex.query.QueryException;
import org.basex.query.value.item.QNm;
import org.basex.query.value.node.ANode;
import org.basex.query.value.node.DBNode;
import org.basex.query.value.type.NodeType;
import org.basex.util.InputInfo;

/**
 * Parses the jar descriptors and performs schema checks.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Rositsa Shadura
 */
final class JarParser {
  /** Input info. */
  private final InputInfo info;

  /**
   * Constructor.
   * @param info input info
   */
  JarParser(final InputInfo info) {
    this.info = info;
  }

  /**
   * Parses a jar descriptor.
   * @param io XML input
   * @return jar descriptor container
   * @throws QueryException query exception
   */
  public JarDesc parse(final IO io) throws QueryException {
    final JarDesc desc = new JarDesc();
    try {
      final ANode node = new DBNode(io).children().next();
      for(final ANode next : node.children()) {
        if(next.type != NodeType.ELM) continue;

        final QNm name = next.qname();
        // ignore namespace to improve compatibility
        if(eq(JAR, name.local())) desc.jars.add(next.string());
        else if(eq(CLASS, name.local())) desc.classes.add(next.string());
        // [CG] Packaging: add warning if unknown elements are encountered
      }
      if(desc.jars.isEmpty()) throw BXRE_JARDESC_X.get(info, NOJARS);
      if(desc.classes.isEmpty()) throw BXRE_JARDESC_X.get(info, NOCLASSES);
      return desc;
    } catch(final IOException ex) {
      throw BXRE_JARFAIL_X.get(info, ex);
    }
  }
}
