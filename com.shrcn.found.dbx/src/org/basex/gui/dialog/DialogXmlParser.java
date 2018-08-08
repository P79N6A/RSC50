package org.basex.gui.dialog;

import static org.basex.core.Text.BROWSE_D;
import static org.basex.core.Text.CHOP_WS;
import static org.basex.core.Text.FILE_OR_DIR;
import static org.basex.core.Text.HELP1_USE_CATALOG;
import static org.basex.core.Text.HELP2_USE_CATALOG;
import static org.basex.core.Text.H_CHOP_WS;
import static org.basex.core.Text.H_INT_PARSER;
import static org.basex.core.Text.INT_PARSER;
import static org.basex.core.Text.PARSE_DTDS;
import static org.basex.core.Text.STRIP_NS;
import static org.basex.core.Text.USE_CATALOG_FILE;
import static org.basex.core.Text.USE_XINCLUDE;
import static org.basex.core.Text.XML_DOCUMENTS;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.basex.build.xml.CatalogWrapper;
import org.basex.core.MainOptions;
import org.basex.gui.GUI;
import org.basex.gui.GUIConstants;
import org.basex.gui.GUIOptions;
import org.basex.gui.layout.BaseXBack;
import org.basex.gui.layout.BaseXButton;
import org.basex.gui.layout.BaseXCheckBox;
import org.basex.gui.layout.BaseXDialog;
import org.basex.gui.layout.BaseXFileChooser;
import org.basex.gui.layout.BaseXFileChooser.Mode;
import org.basex.gui.layout.BaseXLabel;
import org.basex.gui.layout.BaseXTextField;
import org.basex.gui.layout.TableLayout;
import org.basex.io.IO;

/**
 * CSV parser panel.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class DialogXmlParser extends DialogParser {
  /** Internal XML parsing. */
  private final BaseXCheckBox intparse;
  /** DTD mode. */
  private final BaseXCheckBox dtd;
  /** Whitespace chopping. */
  private final BaseXCheckBox chopWS;
  /** Namespace stripping. */
  private final BaseXCheckBox stripNS;
  /** Use XML Catalog. */
  private final BaseXCheckBox usecat;
  /** Use XInclude. */
  private final BaseXCheckBox xinclude;
  /** Catalog file. */
  private final BaseXTextField cfile;
  /** Browse Catalog file. */
  private final BaseXButton browsec;

  /**
   * Constructor.
   * @param d dialog reference
   * @param opts main options
   */
  DialogXmlParser(final BaseXDialog d, final MainOptions opts) {
    super(d);
    final BaseXBack pp = new BaseXBack(new TableLayout(10, 1));

    intparse = new BaseXCheckBox(INT_PARSER, MainOptions.INTPARSE, opts, d).bold();
    pp.add(intparse);
    pp.add(new BaseXLabel(H_INT_PARSER, true, false));

    dtd = new BaseXCheckBox(PARSE_DTDS, MainOptions.DTD, opts, d).bold();
    pp.add(dtd);

    stripNS = new BaseXCheckBox(STRIP_NS, MainOptions.STRIPNS, opts, d).bold();
    pp.add(stripNS);

    chopWS = new BaseXCheckBox(CHOP_WS, MainOptions.CHOP, opts, d).bold();
    pp.add(chopWS);
    pp.add(new BaseXLabel(H_CHOP_WS, false, false).border(0, 0, 8, 0));
    pp.add(new BaseXLabel());

    // XInclude
    xinclude = new BaseXCheckBox(USE_XINCLUDE, MainOptions.XINCLUDE, opts, d).bold();
    pp.add(xinclude);

    // catalog resolver
    final boolean cat = !opts.get(MainOptions.CATFILE).isEmpty();
    usecat = new BaseXCheckBox(USE_CATALOG_FILE, cat, d).bold();
    final boolean rsen = CatalogWrapper.available();
    final BaseXBack cr = new BaseXBack(new TableLayout(2, 2, 8, 0));
    usecat.setEnabled(rsen);
    cr.add(usecat);
    cr.add(new BaseXLabel());

    cfile = new BaseXTextField(opts.get(MainOptions.CATFILE), d);
    cfile.setEnabled(rsen);
    cr.add(cfile);

    browsec = new BaseXButton(BROWSE_D, d);
    browsec.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        final GUIOptions gopts = dialog.gui.gopts;
        final BaseXFileChooser fc = new BaseXFileChooser(FILE_OR_DIR,
          gopts.get(GUIOptions.INPUTPATH), dialog.gui).filter(XML_DOCUMENTS, IO.XMLSUFFIX);

        final IO file = fc.select(Mode.FDOPEN);
        if(file != null) cfile.setText(file.path());
      }
    });
    browsec.setEnabled(rsen);
    cr.add(browsec);
    pp.add(cr);
    if(!rsen) {
      final BaseXBack rs = new BaseXBack(new TableLayout(2, 1));
      rs.add(new BaseXLabel(HELP1_USE_CATALOG).color(GUIConstants.dgray));
      rs.add(new BaseXLabel(HELP2_USE_CATALOG).color(GUIConstants.dgray));
      pp.add(rs);
    }

    add(pp, BorderLayout.WEST);
    action(true);
  }

  @Override
  boolean action(final boolean active) {
    final boolean ip = intparse.isSelected();
    final boolean uc = usecat.isSelected();
    intparse.setEnabled(!uc);
    xinclude.setEnabled(!ip);
    usecat.setEnabled(!ip && CatalogWrapper.available());
    cfile.setEnabled(uc);
    browsec.setEnabled(uc);
    return true;
  }

  @Override
  void update() {
  }

  @Override
  void setOptions(final GUI gui) {
    gui.set(MainOptions.CHOP, chopWS.isSelected());
    gui.set(MainOptions.STRIPNS, stripNS.isSelected());
    gui.set(MainOptions.DTD, dtd.isSelected());
    gui.set(MainOptions.INTPARSE, intparse.isSelected());
    gui.set(MainOptions.XINCLUDE, xinclude.isSelected());
    gui.set(MainOptions.CATFILE, usecat.isSelected() ? cfile.getText() : "");
  }
}
