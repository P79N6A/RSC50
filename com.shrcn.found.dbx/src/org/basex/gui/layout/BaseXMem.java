package org.basex.gui.layout;

import static org.basex.gui.GUIConstants.BACK;
import static org.basex.gui.GUIConstants.CURSORHAND;
import static org.basex.gui.GUIConstants.color1;
import static org.basex.gui.GUIConstants.color3;
import static org.basex.gui.GUIConstants.colormark3;
import static org.basex.gui.GUIConstants.colormark4;
import static org.basex.gui.GUIConstants.dgray;
import static org.basex.gui.GUIConstants.gray;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import org.basex.gui.dialog.DialogMem;
import org.basex.util.Performance;

/**
 * This component visualizes the current memory consumption.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class BaseXMem extends BaseXPanel {
  /** Default width of the memory status box. */
  private static final int DWIDTH = 70;

  /**
   * Constructor.
   * @param win parent reference
   * @param mouse mouse interaction
   */
  public BaseXMem(final Window win, final boolean mouse) {
    super(win);
    BaseXLayout.setWidth(this, DWIDTH);
    setPreferredSize(new Dimension(getPreferredSize().width, getFont().getSize() + 6));
    if(mouse) {
      setCursor(CURSORHAND);
      addMouseListener(this);
      addMouseMotionListener(this);
    }

    // regularly refresh panel
    new Timer(true).scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() { repaint(); }
    }, 0, 5000);
  }

  @Override
  public void paintComponent(final Graphics g) {
    super.paintComponent(g);

    final Runtime rt = Runtime.getRuntime();
    final long max = rt.maxMemory();
    final long total = rt.totalMemory();
    final long used = total - rt.freeMemory();
    final int ww = getWidth();
    final int hh = getHeight();

    // draw memory box
    g.setColor(BACK);
    g.fillRect(0, 0, ww - 3, hh - 3);
    g.setColor(gray);
    g.drawLine(0, 0, ww - 4, 0);
    g.drawLine(0, 0, 0, hh - 4);
    g.drawLine(ww - 3, 0, ww - 3, hh - 3);
    g.drawLine(0, hh - 3, ww - 3, hh - 3);

    // show total memory usage
    g.setColor(color1);
    g.fillRect(2, 2, Math.max(1, (int) (total * (ww - 6) / max)), hh - 6);

    // show current memory usage
    final boolean full = used * 6 / 5 > max;
    g.setColor(full ? colormark4 : color3);
    g.fillRect(2, 2, Math.max(1, (int) (used * (ww - 6) / max)), hh - 6);

    // print current memory usage
    final FontMetrics fm = g.getFontMetrics();
    final String sz = Performance.format(used, true);
    final int fw = (ww - fm.stringWidth(sz)) / 2;
    final int h = fm.getHeight() - 3;
    g.setColor(full ? colormark3 : dgray);
    g.drawString(sz, fw, h);
  }

  @Override
  public void mousePressed(final MouseEvent e) {
    DialogMem.show(gui);
    repaint();
  }
}
