package org.basex.api.dom;

import org.basex.query.value.node.ANode;
import org.w3c.dom.DocumentFragment;

/**
 * DOM - Document fragment implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class BXDocFrag extends BXNode implements DocumentFragment {
  /**
   * Constructor.
   * @param node node reference
   */
  BXDocFrag(final ANode node) {
    super(node);
  }

  @Override
  protected int kind() {
    return 7;
  }
}
