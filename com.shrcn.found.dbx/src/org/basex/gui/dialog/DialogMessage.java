package org.basex.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.SwingUtilities;

import org.basex.core.Text;
import org.basex.gui.GUI;
import org.basex.gui.GUIConstants.Msg;
import org.basex.gui.layout.BaseXBack;
import org.basex.gui.layout.BaseXButton;
import org.basex.gui.layout.BaseXDialog;
import org.basex.gui.layout.BaseXLabel;
import org.basex.gui.layout.TableLayout;
import org.basex.gui.text.TextPanel;

/**
 * Dialog window for messages.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DialogMessage extends BaseXDialog {
  /** Taken action. */
  private String action;

  /**
   * Default constructor.
   * @param main reference to the main window
   * @param txt message text
   * @param ic message type
   * @param buttons additional buttons
   */
  public DialogMessage(final GUI main, final String txt, final Msg ic, final String... buttons) {
    super(main, ic == Msg.ERROR ? Text.ERROR : Text.INFORMATION);

    panel.setLayout(new BorderLayout());

    final BaseXBack back = new BaseXBack(new TableLayout(1, 2, 12, 0));
    final BaseXLabel label = new BaseXLabel();
    label.setIcon(ic.large);
    back.add(label);

    final TextPanel text = new TextPanel(txt, false, this);
    text.setFont(label.getFont());
    back.add(text);

    set(back, BorderLayout.NORTH);

    final ArrayList<Object> list = new ArrayList<>();
    if(ic == Msg.QUESTION || ic == Msg.YESNOCANCEL) {
      list.add(Text.B_YES);
      list.add(Text.B_NO);
      Collections.addAll(list, buttons);
      if(ic == Msg.YESNOCANCEL) list.add(Text.B_CANCEL);
    } else {
      Collections.addAll(list, buttons);
      list.add(Text.B_OK);
    }
    final BaseXBack bttns = newButtons(list.toArray(new Object[list.size()]));
    set(bttns, BorderLayout.SOUTH);

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        ((Container) bttns.getComponent(0)).getComponent(0).requestFocusInWindow();
      }
    });
    finish();
  }

  @Override
  public void action(final Object cmp) {
    final BaseXButton button = (BaseXButton) cmp;
    final String text = button.getText();
    if(text.equals(Text.B_CANCEL)) cancel();

    action = text;
    if(text.equals(Text.NO)) {
      cancel();
    } else {
      close();
    }
  }

  /**
   * Returns the chosen action.
   * @return action
   */
  public String action() {
    return action;
  }
}
