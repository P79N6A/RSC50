package org.basex.gui.dialog;

import static org.basex.core.Text.COL;
import static org.basex.core.Text.ENCODING;
import static org.basex.core.Text.SPLIT_INPUT_LINES;

import java.awt.BorderLayout;

import org.basex.build.text.TextOptions;
import org.basex.core.MainOptions;
import org.basex.gui.GUI;
import org.basex.gui.layout.BaseXBack;
import org.basex.gui.layout.BaseXCheckBox;
import org.basex.gui.layout.BaseXCombo;
import org.basex.gui.layout.BaseXDialog;
import org.basex.gui.layout.BaseXLabel;
import org.basex.gui.layout.TableLayout;
import org.basex.util.Strings;

/**
 * CSV parser panel.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class DialogTextParser extends DialogParser {
  /** Options. */
  private final TextOptions topts;
  /** Text: encoding. */
  private final BaseXCombo encoding;
  /** Text: Use lines. */
  private final BaseXCheckBox lines;

  /**
   * Constructor.
   * @param d dialog reference
   * @param opts main options
   */
  DialogTextParser(final BaseXDialog d, final MainOptions opts) {
    super(d);
    topts = new TextOptions(opts.get(MainOptions.TEXTPARSER));

    final BaseXBack pp  = new BaseXBack(new TableLayout(2, 1, 0, 8));

    encoding = DialogExport.encoding(d, topts.get(TextOptions.ENCODING));
    lines = new BaseXCheckBox(SPLIT_INPUT_LINES, TextOptions.LINES, topts, d);

    final BaseXBack p = new BaseXBack(new TableLayout(1, 2, 8, 4));
    p.add(new BaseXLabel(ENCODING + COL, true, true));
    p.add(encoding);
    pp.add(p);
    pp.add(lines);

    add(pp, BorderLayout.WEST);
  }

  @Override
  boolean action(final boolean active) {
    return true;
  }

  @Override
  void update() {
    final String enc = encoding.getSelectedItem();
    topts.set(TextOptions.ENCODING, enc.equals(Strings.UTF8) ? null : enc);
    topts.set(TextOptions.LINES, lines.isSelected());
  }

  @Override
  void setOptions(final GUI gui) {
    gui.set(MainOptions.TEXTPARSER, topts);
  }
}
