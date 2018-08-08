package org.basex.gui.view.project;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.basex.gui.GUIConstants;
import org.basex.gui.layout.BaseXImages;

/**
 * Custom tree cell renderer to distinguish between raw and xml leaf nodes.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class ProjectCellRenderer extends DefaultTreeCellRenderer {
  @Override
  public Component getTreeCellRendererComponent(final JTree tree, final Object val,
      final boolean select, final boolean expanded, final boolean leaf, final int row,
      final boolean focus) {

    final Color tnsc = getTextNonSelectionColor(), tsc = getTextSelectionColor();
    if(val instanceof ProjectNode && ((ProjectNode) val).error) {
      textNonSelectionColor = GUIConstants.RED;
      textSelectionColor = GUIConstants.RED;
    }
    super.getTreeCellRendererComponent(tree, val, select, expanded, leaf, row, focus);
    setIcon(icon(val, expanded));
    textNonSelectionColor = tnsc;
    textSelectionColor = tsc;
    return this;
  }

  /**
   * Returns an icon for the specified value.
   * @param val value
   * @param expanded expanded flag
   * @return icon
   */
  static Icon icon(final Object val, final boolean expanded) {
    return val instanceof ProjectFile ? BaseXImages.file(((ProjectFile) val).file) :
      BaseXImages.dir(expanded);
  }
}
