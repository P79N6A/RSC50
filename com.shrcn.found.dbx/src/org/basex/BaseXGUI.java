package org.basex;

import static org.basex.core.Text.CREATE_DB_FILE;
import static org.basex.core.Text.S_CONSOLE_X;
import static org.basex.core.Text.S_GUI;
import static org.basex.core.Text.S_GUIINFO;

import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.Check;
import org.basex.gui.GUI;
import org.basex.gui.GUIConstants;
import org.basex.gui.GUIMacOSX;
import org.basex.gui.GUIOptions;
import org.basex.gui.dialog.DialogProgress;
import org.basex.gui.layout.BaseXDialog;
import org.basex.io.IO;
import org.basex.io.IOFile;
import org.basex.util.Main;
import org.basex.util.MainParser;
import org.basex.util.Prop;
import org.basex.util.Util;
import org.basex.util.list.StringList;

/**
 * This is the starter class for the graphical frontend.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class BaseXGUI extends Main {
  /** Database context. */
  private final Context context = new Context();
  /** Files, specified as arguments. */
  private final StringList files = new StringList(0);
  /** Mac OS X GUI optimizations. */
  GUIMacOSX osxGUI;

  /**
   * Main method.
   * @param args text files to open: XML documents, queries, etc.
   */
  public static void main(final String... args) {
    try {
      new BaseXGUI(args);
    } catch(final BaseXException ex) {
      Util.errln(ex);
      System.exit(1);
    }
  }

  /**
   * Constructor.
   * @param args command-line arguments
   * @throws BaseXException database exception
   */
  public BaseXGUI(final String... args) throws BaseXException {
    super(args);
    parseArgs();

    // set Mac-specific properties
    if(Prop.MAC) {
      try {
        osxGUI = new GUIMacOSX();
      } catch(final Exception ex) {
        throw new BaseXException("Failed to initialize native Mac OS X interface", ex);
      }
    }

    // read options
    final GUIOptions gopts = new GUIOptions();
    // initialize look and feel
    init(gopts);
    // initialize fonts and colors
    GUIConstants.init(gopts);

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        // open main window
        final GUI gui = new GUI(context, gopts);
        if(osxGUI != null) osxGUI.init(gui);

        // open specified file
        final ArrayList<IOFile> xqfiles = new ArrayList<>();
        for(final String file : files.finish()) {
          if(file.contains(IO.BASEXSUFFIX)) continue;

          final IOFile io = new IOFile(file);
          final boolean xml = file.endsWith(IO.XMLSUFFIX);
          if(xml && BaseXDialog.confirm(gui, Util.info(CREATE_DB_FILE, io))) {
            gopts.set(GUIOptions.INPUTPATH, io.path());
            gopts.set(GUIOptions.DBNAME, io.dbName());
            DialogProgress.execute(gui, new Check(file));
          } else {
            xqfiles.add(io);
          }
        }
        gui.editor.init(xqfiles);
      }
    });

    // guarantee correct shutdown of database context
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public synchronized void run() {
        context.close();
      }
    });
  }

  /**
   * Initializes the GUI.
   * @param opts gui options
   */
  private static void init(final GUIOptions opts) {
    try {
      // refresh views when windows are resized
      Toolkit.getDefaultToolkit().setDynamicLayout(true);
      // set look and feel
      final String laf = opts.get(GUIOptions.LOOKANDFEEL);
      UIManager.setLookAndFeel(laf.isEmpty() ? UIManager.getSystemLookAndFeelClassName() : laf);
    } catch(final Exception ex) {
      Util.stack(ex);
    }
  }

  @Override
  protected void parseArgs() throws BaseXException {
    final MainParser arg = new MainParser(this);
    while(arg.more()) {
      if(arg.dash()) {
        final char c = arg.next();
        if(c == 'd') {
          // activate debug mode
          Prop.debug = true;
        } else {
          throw arg.usage();
        }
      } else {
        files.add(arg.string());
      }
    }
  }

  @Override
  public String header() {
    return Util.info(S_CONSOLE_X, S_GUI);
  }

  @Override
  public String usage() {
    return S_GUIINFO;
  }
}
