package org.basex.gui.dialog;

import static org.basex.core.Text.H_ATTR_INDEX;
import static org.basex.core.Text.H_TEXT_INDEX;
import static org.basex.core.Text.H_TOKEN_INDEX;

import org.basex.core.MainOptions;
import org.basex.gui.layout.BaseXDialog;
import org.basex.gui.layout.BaseXLabel;
import org.basex.gui.layout.BaseXTextField;
import org.basex.gui.layout.TableLayout;
import org.basex.index.IndexType;
import org.basex.util.options.StringOption;

/**
 * Value index creation dialog.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class DialogValues extends DialogIndex {
  /** Names to include. */
  private final BaseXTextField include;
  /** Option. */
  private final StringOption inc;

  /**
   * Constructor.
   * @param dialog dialog reference
   * @param type index type
   */
  DialogValues(final BaseXDialog dialog, final IndexType type) {
    super(dialog);

    layout(new TableLayout(2, 1));

    final MainOptions opts = dialog.gui.context.options;
    final String text = type == IndexType.TOKEN ? H_TOKEN_INDEX : type == IndexType.TEXT
        ? H_TEXT_INDEX : H_ATTR_INDEX;
    add(new BaseXLabel(text, true, false).border(0, 0, 8, 0));

    inc = type == IndexType.TOKEN ? MainOptions.TOKENINCLUDE : type == IndexType.TEXT
        ? MainOptions.TEXTINCLUDE : MainOptions.ATTRINCLUDE;
    include = new BaseXTextField(opts.get(inc), dialog).hint(QNAME_INPUT);
    add(include);
  }

  @Override
  void action(final boolean enabled) {
    include.setEnabled(enabled);
  }

  @Override
  void setOptions() {
    dialog.gui.set(inc, include.getText());
  }
}
