package org.basex.gui.dialog;

import static org.basex.core.Text.ADD;
import static org.basex.core.Text.ADD_RESOURCES;
import static org.basex.core.Text.COLS;
import static org.basex.core.Text.DOTS;
import static org.basex.core.Text.GENERAL;
import static org.basex.core.Text.INVALID_X;
import static org.basex.core.Text.PARSING;
import static org.basex.core.Text.RES_NOT_FOUND;
import static org.basex.core.Text.TARGET_PATH;

import java.awt.BorderLayout;

import org.basex.core.cmd.Add;
import org.basex.data.MetaData;
import org.basex.gui.GUIConstants.Msg;
import org.basex.gui.layout.BaseXBack;
import org.basex.gui.layout.BaseXButton;
import org.basex.gui.layout.BaseXLabel;
import org.basex.gui.layout.BaseXTabs;
import org.basex.gui.layout.BaseXTextField;
import org.basex.gui.layout.TableLayout;
import org.basex.util.Util;

/**
 * Panel for adding new resources.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Lukas Kircher
 */
final class DialogAdd extends BaseXBack {
  /** Dialog reference. */
  private final DialogProps dialog;
  /** Target path. */
  final BaseXTextField target;

  /** General options. */
  private final DialogImport general;
  /** Add button. */
  private final BaseXButton add;

  /**
   * Constructor.
   * @param d dialog reference
   */
  DialogAdd(final DialogProps d) {
    dialog = d;
    setLayout(new BorderLayout());

    add(new BaseXLabel(ADD_RESOURCES).large().border(0,  0, 16, 0), BorderLayout.NORTH);

    target = new BaseXTextField("/", d);

    final BaseXBack pnl = new BaseXBack(new TableLayout(2, 1));
    pnl.add(new BaseXLabel(TARGET_PATH + COLS, true, true).border(8, 0, 6, 0));
    pnl.add(target);

    // option panels
    final BaseXTabs tabs = new BaseXTabs(d);
    final DialogParsing parsing = new DialogParsing(d, tabs);
    general = new DialogImport(d, pnl, parsing);

    tabs.addTab(GENERAL, general);
    tabs.addTab(PARSING, parsing);
    add(tabs, BorderLayout.CENTER);

    // buttons
    add = new BaseXButton(ADD + DOTS, d);

    add(d.newButtons(add), BorderLayout.SOUTH);
    action(general.parsers);
  }

  /**
   * Reacts on user input.
   * @param comp the action component
   */
  void action(final Object comp) {
    final String src = general.input();
    final String trg = target.getText().trim();

    if(comp == add) {
      general.setOptions();
      final Runnable run = new Runnable() {
        @Override
        public void run() {
          dialog.resources.refreshNewFolder(trg);
        }
      };
      DialogProgress.execute(dialog, run, new Add(trg, src));

    } else {
      boolean ok = general.action(comp, false);
      if(comp == general.browse || comp == general.input) target.setText(general.dbName);

      final String inf;
      if(ok) {
        // check if target path is valid
        ok = MetaData.normPath(trg) != null;
        inf = ok ? null : Util.info(INVALID_X, TARGET_PATH);
      } else {
        inf = RES_NOT_FOUND;
      }
      general.info.setText(inf, Msg.ERROR);
      add.setEnabled(ok);
    }
  }
}
