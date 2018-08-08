package org.basex.gui.dialog;

import static org.basex.core.Text.ATTRIBUTE_INDEX;
import static org.basex.core.Text.B_OK;
import static org.basex.core.Text.COLS;
import static org.basex.core.Text.CREATE_DATABASE;
import static org.basex.core.Text.EMPTY_DB;
import static org.basex.core.Text.ENTER_DB_NAME;
import static org.basex.core.Text.FULLTEXT;
import static org.basex.core.Text.FULLTEXT_INDEX;
import static org.basex.core.Text.GENERAL;
import static org.basex.core.Text.INDEXES;
import static org.basex.core.Text.INVALID_X;
import static org.basex.core.Text.NAME;
import static org.basex.core.Text.NAME_OF_DB;
import static org.basex.core.Text.OPTIONS;
import static org.basex.core.Text.OVERWRITE_DB;
import static org.basex.core.Text.PARSING;
import static org.basex.core.Text.RES_NOT_FOUND;
import static org.basex.core.Text.TEXT_INDEX;
import static org.basex.core.Text.TOKEN_INDEX;

import java.awt.BorderLayout;

import org.basex.core.Databases;
import org.basex.core.MainOptions;
import org.basex.gui.GUI;
import org.basex.gui.GUIConstants.Msg;
import org.basex.gui.GUIOptions;
import org.basex.gui.layout.BaseXBack;
import org.basex.gui.layout.BaseXCheckBox;
import org.basex.gui.layout.BaseXDialog;
import org.basex.gui.layout.BaseXLabel;
import org.basex.gui.layout.BaseXTabs;
import org.basex.gui.layout.BaseXTextField;
import org.basex.gui.layout.TableLayout;
import org.basex.index.IndexType;
import org.basex.util.Util;

/**
 * Dialog window for specifying options for creating a new database.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DialogNew extends BaseXDialog {
  /** General dialog. */
  private final DialogImport general;
  /** Options dialog. */
  private final DialogOptions options;
  /** Database name. */
  private final BaseXTextField dbName;
  /** Buttons. */
  private final BaseXBack buttons;

  /** Text index flag. */
  private final BaseXCheckBox textindex;
  /** Attribute value index flag. */
  private final BaseXCheckBox attrindex;
  /** Full-text index flag. */
  private final BaseXCheckBox ftindex;
  /** Token index flag. */
  private final BaseXCheckBox tokenindex;
  /** Index creation options. */
  private final DialogIndex[] index;

  /**
   * Default constructor.
   * @param main reference to the main window
   */
  public DialogNew(final GUI main) {
    super(main, CREATE_DATABASE);

    // define buttons first to assign simplest mnemonics
    buttons = okCancel();

    final MainOptions opts = gui.context.options;
    final GUIOptions gopts = main.gopts;

    dbName = new BaseXTextField(gopts.get(GUIOptions.DBNAME), this);

    final BaseXBack pnl = new BaseXBack(new TableLayout(2, 1));
    pnl.add(new BaseXLabel(NAME_OF_DB + COLS, false, true).border(8, 0, 6, 0));
    pnl.add(dbName);

    // option panels
    final BaseXTabs tabs = new BaseXTabs(this);
    final DialogParsing parsePanel = new DialogParsing(this, tabs);
    general = new DialogImport(this, pnl, parsePanel);

    index = new DialogIndex[] {
      new DialogValues(this, IndexType.TEXT), new DialogValues(this, IndexType.ATTRIBUTE),
      new DialogValues(this, IndexType.TOKEN), new DialogFT(this, true)
    };
    textindex = new BaseXCheckBox(TEXT_INDEX, MainOptions.TEXTINDEX, opts, this).bold().large();
    attrindex = new BaseXCheckBox(ATTRIBUTE_INDEX, MainOptions.ATTRINDEX, opts, this).bold().
        large();
    tokenindex = new BaseXCheckBox(TOKEN_INDEX, MainOptions.TOKENINDEX, opts, this).bold().large();
    ftindex = new BaseXCheckBox(FULLTEXT_INDEX, MainOptions.FTINDEX, opts, this).bold().large();

    // index panel
    final BaseXBack indexPanel = new BaseXBack(new TableLayout(8, 1)).border(8);
    indexPanel.add(textindex);
    indexPanel.add(index[0]);
    indexPanel.add(new BaseXBack());
    indexPanel.add(attrindex);
    indexPanel.add(index[1]);
    indexPanel.add(new BaseXBack());
    indexPanel.add(tokenindex);
    indexPanel.add(index[2]);

    // full-text index panel
    final BaseXBack ftPanel = new BaseXBack(new TableLayout(2, 1)).border(8);
    ftPanel.add(ftindex);
    ftPanel.add(index[3]);

    // options panel
    options = new DialogOptions(this, null);

    tabs.addTab(GENERAL, general);
    tabs.addTab(PARSING, parsePanel);
    tabs.addTab(INDEXES, indexPanel);
    tabs.addTab(FULLTEXT, ftPanel);
    tabs.addTab(OPTIONS, options);
    set(tabs, BorderLayout.CENTER);

    set(buttons, BorderLayout.SOUTH);

    general.setType(general.input());
    action(general.parsers);

    setResizable(true);
    finish();
  }

  @Override
  public void action(final Object comp) {
    final boolean valid = general.action(comp, true) && options.action();
    index[0].action(textindex.isSelected());
    index[1].action(attrindex.isSelected());
    index[2].action(tokenindex.isSelected());
    index[3].action(ftindex.isSelected());

    // ...must be located before remaining checks
    if(comp == general.browse || comp == general.input) dbName.setText(general.dbName);

    final String nm = dbName.getText().trim();
    ok = valid && !nm.isEmpty();

    String inf = valid ? ok ? null : ENTER_DB_NAME : RES_NOT_FOUND;
    Msg icon = Msg.ERROR;
    if(ok) {
      ok = Databases.validName(nm);
      if(ok) gui.gopts.set(GUIOptions.DBNAME, nm);

      if(!ok) {
        // name of database is invalid
        inf = Util.info(INVALID_X, NAME);
      } else if(general.input.getText().trim().isEmpty()) {
        // database will be empty
        inf = EMPTY_DB;
        icon = Msg.WARN;
      } else if(gui.context.databases.listDBs().contains(nm)) {
        // old database will be overwritten
        inf = OVERWRITE_DB;
        icon = Msg.WARN;
      }
    }

    general.info.setText(inf, icon);
    enableOK(buttons, B_OK, ok);
  }

  @Override
  public void close() {
    if(!ok) return;

    super.close();
    gui.set(MainOptions.TEXTINDEX, textindex.isSelected());
    gui.set(MainOptions.ATTRINDEX, attrindex.isSelected());
    gui.set(MainOptions.TOKENINDEX, tokenindex.isSelected());
    gui.set(MainOptions.FTINDEX, ftindex.isSelected());
    general.setOptions();
    options.setOptions(null);
    for(final DialogIndex di : index) di.setOptions();
  }
}
