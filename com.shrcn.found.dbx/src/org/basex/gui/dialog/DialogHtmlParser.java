package org.basex.gui.dialog;

import static org.basex.core.Text.COL;
import static org.basex.core.Text.H_HTML_PARSER;
import static org.basex.core.Text.H_NO_HTML_PARSER;
import static org.basex.core.Text.PARAMETERS;

import java.awt.BorderLayout;
import java.io.IOException;

import org.basex.build.html.HtmlOptions;
import org.basex.build.html.HtmlParser;
import org.basex.core.MainOptions;
import org.basex.gui.GUI;
import org.basex.gui.GUIConstants.Msg;
import org.basex.gui.layout.BaseXBack;
import org.basex.gui.layout.BaseXDialog;
import org.basex.gui.layout.BaseXLabel;
import org.basex.gui.layout.BaseXTextField;
import org.basex.gui.layout.TableLayout;
import org.basex.util.options.Option;

/**
 * CSV parser panel.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class DialogHtmlParser extends DialogParser {
  /** Options. */
  private final HtmlOptions hopts;
  /** Parameters. */
  private final BaseXTextField options;
  /** User feedback. */
  private final BaseXLabel info;

  /**
   * Constructor.
   * @param d dialog reference
   * @param opts main options
   */
  DialogHtmlParser(final BaseXDialog d, final MainOptions opts) {
    super(d);
    hopts = new HtmlOptions(opts.get(MainOptions.HTMLPARSER));

    final boolean avl = HtmlParser.available();
    final BaseXBack pp  = new BaseXBack(new TableLayout(3, 1, 0, 8));
    pp.add(new BaseXLabel(avl ? H_HTML_PARSER : H_NO_HTML_PARSER));

    options = new BaseXTextField(hopts.toString(), d);
    options.setToolTipText(tooltip(hopts));

    if(avl) {
      final BaseXBack p = new BaseXBack(new TableLayout(1, 2, 8, 0));
      p.add(new BaseXLabel(PARAMETERS + COL, true, true));
      p.add(options);
      pp.add(p);
    }

    info = new BaseXLabel(" ").border(12, 0, 6, 0);
    pp.add(info);

    add(pp, BorderLayout.WEST);
  }

  @Override
  boolean action(final boolean active) {
    try {
      hopts.assign(options.getText());
      info.setText(null, null);
      return true;
    } catch(final IOException ex) {
      info.setText(ex.getMessage(), Msg.ERROR);
      return false;
    }
  }

  @Override
  void update() {
  }

  @Override
  void setOptions(final GUI gui) {
    gui.set(MainOptions.HTMLPARSER, hopts);
  }

  /**
   * Returns a tooltip for the specified options string.
   * @param opts serialization options
   * @return string
   */
  private static String tooltip(final HtmlOptions opts) {
    final StringBuilder sb = new StringBuilder("<html><b>").append(PARAMETERS).append(":</b><br>");
    for(final Option<?> so : opts) {
      sb.append("\u2022 ").append(so).append("<br/>");
    }
    return sb.append("</html>").toString();
  }
}
