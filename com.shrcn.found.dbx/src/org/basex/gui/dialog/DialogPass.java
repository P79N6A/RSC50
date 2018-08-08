package org.basex.gui.dialog;

import static org.basex.core.Text.ALTER_PW;
import static org.basex.core.Text.B_CANCEL;
import static org.basex.core.Text.B_OK;
import static org.basex.core.Text.INVALID_X;
import static org.basex.core.Text.PASSWORD;

import java.awt.BorderLayout;

import org.basex.gui.GUI;
import org.basex.gui.GUIConstants.Msg;
import org.basex.gui.layout.BaseXBack;
import org.basex.gui.layout.BaseXDialog;
import org.basex.gui.layout.BaseXLabel;
import org.basex.gui.layout.BaseXPassword;
import org.basex.util.Util;

/**
 * Password dialog.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DialogPass extends BaseXDialog {
  /** New password. */
  private final BaseXPassword pass;
  /** Buttons. */
  private final BaseXBack buttons;
  /** Info label. */
  private final BaseXLabel info;

  /**
   * Default constructor.
   * @param main reference to the main window
   */
  public DialogPass(final GUI main) {
    super(main, ALTER_PW);

    pass = new BaseXPassword(this);
    pass.addKeyListener(keys);
    info = new BaseXLabel(" ");

    final BaseXBack p = new BaseXBack(new BorderLayout(0, 8));
    p.add(pass, BorderLayout.NORTH);
    p.add(info, BorderLayout.CENTER);
    set(p, BorderLayout.CENTER);

    buttons = newButtons(B_OK, B_CANCEL);
    set(buttons, BorderLayout.SOUTH);
    action(null);
    finish();
  }

  @Override
  public void action(final Object cmp) {
    final String nm = password();
    ok = nm.matches("[^ ;'\"]*");
    info.setText(ok ? null : Util.info(INVALID_X, PASSWORD), Msg.ERROR);
    enableOK(buttons, B_OK, ok);
  }

  @Override
  public void close() {
    if(!ok) return;
    super.close();
  }

  /**
   * Returns the password.
   * @return password
   */
  public String password() {
    return new String(pass.getPassword());
  }
}
