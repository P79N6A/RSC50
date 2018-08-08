package org.basex.gui.dialog;

import static org.basex.core.Text.B_CANCEL;
import static org.basex.core.Text.B_OK;
import static org.basex.core.Text.COLS;
import static org.basex.core.Text.DB_EXISTS_X;
import static org.basex.core.Text.ENTER_DB_NAME;
import static org.basex.core.Text.INVALID_X;
import static org.basex.core.Text.NAME;
import static org.basex.core.Text.NAME_OF_DB;
import static org.basex.core.Text.NAME_OF_DB_COPY;
import static org.basex.core.Text.TARGET_PATH;

import java.awt.BorderLayout;

import org.basex.core.Databases;
import org.basex.data.MetaData;
import org.basex.gui.GUIConstants.Msg;
import org.basex.gui.layout.BaseXBack;
import org.basex.gui.layout.BaseXDialog;
import org.basex.gui.layout.BaseXLabel;
import org.basex.gui.layout.BaseXTextField;
import org.basex.util.Util;
import org.basex.util.list.StringList;

/**
 * Dialog with a single text field.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class DialogInput extends BaseXDialog {
  /** User input. */
  private final BaseXTextField input;
  /** Old input. */
  private final String old;
  /** Buttons. */
  private final BaseXBack buttons;
  /** Info label. */
  private final BaseXLabel info;
  /** Available databases. */
  private final StringList db;
  /** Rename/copy/delete dialog. */
  private final int type;

  /**
   * Default constructor.
   * @param old old input
   * @param title title string
   * @param dialog dialog window
   * @param type type of dialog: 0 = rename database, 1 = drop documents, 2 = copy database
   */
  DialogInput(final String old, final String title, final BaseXDialog dialog, final int type) {
    super(dialog, title);
    this.old = old;
    this.type = type;
    db = dialog.gui.context.databases.listDBs();

    String t = "";
    if(type == 0) {
      t = TARGET_PATH + COLS;
    } else if(type == 1) {
      t = NAME_OF_DB + COLS;
    } else if(type == 2) {
      t = NAME_OF_DB_COPY + COLS;
    }

    set(new BaseXLabel(t, false, true).border(0, 0, 6, 0), BorderLayout.NORTH);

    input = new BaseXTextField(old, this);
    info = new BaseXLabel(" ");

    final BaseXBack p = new BaseXBack(new BorderLayout(0, 8));
    p.add(input, BorderLayout.NORTH);
    p.add(info, BorderLayout.CENTER);
    set(p, BorderLayout.CENTER);

    buttons = newButtons(B_OK, B_CANCEL);
    set(buttons, BorderLayout.SOUTH);
    action(null);
    finish();
  }

  /**
   * Returns the user input.
   * @return input
   */
  String input() {
    return input.getText().trim();
  }

  @Override
  public void action(final Object cmp) {
    final String in = input();
    ok = type != 0 && (db.contains(in) || in.equals(old));
    String msg = null;
    if(ok) msg = Util.info(DB_EXISTS_X, in);
    if(!ok) {
      ok = type == 0 ? MetaData.normPath(in) != null : Databases.validName(in);
      if(!ok) msg = in.isEmpty() ? ENTER_DB_NAME : Util.info(INVALID_X, NAME);
    }

    info.setText(msg, type == 1 || type == 2 ? Msg.ERROR : Msg.WARN);
    enableOK(buttons, B_OK, ok);
  }

  @Override
  public void close() {
    if(ok) super.close();
  }
}
