package org.basex.gui.dialog;

import static org.basex.core.Text.H_USED_MEM;
import static org.basex.core.Text.MEMUSED_C;
import static org.basex.core.Text.NL;
import static org.basex.core.Text.RESERVED_MEM_C;
import static org.basex.core.Text.TOTAL_MEM_C;
import static org.basex.core.Text.USED_MEM;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import org.basex.gui.GUI;
import org.basex.gui.layout.BaseXBack;
import org.basex.gui.layout.BaseXButton;
import org.basex.gui.layout.BaseXDialog;
import org.basex.gui.text.TextPanel;
import org.basex.util.Performance;

/**
 * Dialog with a single text field.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DialogMem extends BaseXDialog {
  /** Dialog. */
  private static Dialog dialog;

  /** Info text. */
  private final TextPanel text;
  /** GC Button. */
  private final BaseXButton gc;

  /**
   * Default constructor.
   * @param main reference to the main window
   */
  private DialogMem(final GUI main) {
    super(main, USED_MEM, false);
    panel.setLayout(new BorderLayout());

    text = new TextPanel(info(), false, this);
    text.setFont(panel.getFont());
    set(text, BorderLayout.CENTER);

    gc = new BaseXButton("GC", this);
    final BaseXBack buttons = newButtons(gc);
    set(buttons, BorderLayout.SOUTH);
    addTimer();
    finish();
  }

  /**
   * Activates the dialog window.
   * @param main reference to the main window
   */
  public static void show(final GUI main) {
    if(dialog == null) dialog = new DialogMem(main);
    dialog.setVisible(true);
  }

  @Override
  public void setVisible(final boolean v) {
    super.setVisible(v);
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        // focus GC button
        gc.requestFocusInWindow();
      }
    });
  }

  @Override
  public void action(final Object cmp) {
    Performance.gc(3);
    text.setText(info());
  }

  /**
   * Returns the info text.
   * @return text
   */
  private static String info() {
    final Runtime rt = Runtime.getRuntime();
    final long max = rt.maxMemory();
    final long total = rt.totalMemory();
    final long used = total - rt.freeMemory();
    return TOTAL_MEM_C + Performance.format(max, true) + NL
        + RESERVED_MEM_C + Performance.format(total, true) + NL + MEMUSED_C
        + Performance.format(used, true) + NL + NL + H_USED_MEM;
  }


  /**
   * Add timer for updating display of memory consumption.
   */
  private void addTimer() {
    new Timer(true).scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        if(isVisible() && !text.selected()) text.setText(info());
      }
    }, 0, 500);
  }
}
