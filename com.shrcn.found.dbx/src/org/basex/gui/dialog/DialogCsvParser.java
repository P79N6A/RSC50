package org.basex.gui.dialog;

import static org.basex.core.Text.BACKSLASHES;
import static org.basex.core.Text.COL;
import static org.basex.core.Text.ENCODING;
import static org.basex.core.Text.FIRST_LINE_HEADER;
import static org.basex.core.Text.FORMAT;
import static org.basex.core.Text.LAX_NAME_CONVERSION;
import static org.basex.core.Text.PARSE_QUOTES;
import static org.basex.core.Text.SEPARATOR;

import java.awt.BorderLayout;
import java.io.IOException;

import org.basex.build.csv.CsvOptions;
import org.basex.build.csv.CsvOptions.CsvFormat;
import org.basex.build.csv.CsvOptions.CsvSep;
import org.basex.build.csv.CsvParserOptions;
import org.basex.core.MainOptions;
import org.basex.core.MainOptions.MainParser;
import org.basex.gui.GUI;
import org.basex.gui.layout.BaseXBack;
import org.basex.gui.layout.BaseXCheckBox;
import org.basex.gui.layout.BaseXCombo;
import org.basex.gui.layout.BaseXDialog;
import org.basex.gui.layout.BaseXLabel;
import org.basex.gui.layout.BaseXTextField;
import org.basex.gui.layout.TableLayout;
import org.basex.gui.text.TextPanel;
import org.basex.io.IOContent;
import org.basex.io.parse.csv.CsvConverter;
import org.basex.util.Strings;
import org.basex.util.TokenBuilder;
import org.basex.util.list.StringList;

/**
 * CSV parser panel.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class DialogCsvParser extends DialogParser {
  /** CSV example string. */
  private static final String EXAMPLE = "Name,Born?_\n\"John, Adam\\\",1984";

  /** Options. */
  private final CsvParserOptions copts;
  /** JSON example. */
  private final TextPanel example;
  /** CSV: encoding. */
  private final BaseXCombo encoding;
  /** CSV: Use header. */
  private final BaseXCheckBox header;
  /** CSV: format. */
  private final BaseXCombo format;
  /** CSV: Separator. */
  private final BaseXCombo seps;
  /** CSV: Separator (numeric). */
  private final BaseXTextField sepchar;
  /** CSV: Lax name conversion. */
  private final BaseXCheckBox lax;
  /** CSV: Parse quotes. */
  private final BaseXCheckBox quotes;
  /** CSV: Backslashes. */
  private final BaseXCheckBox backslashes;

  /**
   * Constructor.
   * @param d dialog reference
   * @param opts main options
   */
  DialogCsvParser(final BaseXDialog d, final MainOptions opts) {
    super(d);
    copts = new CsvParserOptions(opts.get(MainOptions.CSVPARSER));

    final BaseXBack pp  = new BaseXBack(new TableLayout(2, 1, 0, 8));
    BaseXBack p = new BaseXBack(new TableLayout(4, 2, 8, 4));

    p.add(new BaseXLabel(ENCODING + COL, true, true));
    encoding = DialogExport.encoding(d, copts.get(CsvParserOptions.ENCODING));
    p.add(encoding);

    final BaseXBack sep = new BaseXBack().layout(new TableLayout(1, 2, 6, 0));
    final StringList csv = new StringList();
    for(final CsvSep cs : CsvSep.values()) csv.add(cs.toString());
    final String[] sa = csv.toArray();
    seps = new BaseXCombo(d, csv.add("").finish());
    sep.add(seps);

    final String s = copts.get(CsvOptions.SEPARATOR);
    if(Strings.eq(s, sa)) {
      seps.setSelectedItem(s);
    } else {
      seps.setSelectedIndex(sa.length);
    }
    sepchar = new BaseXTextField(s, d);
    sepchar.setColumns(2);
    sep.add(sepchar);

    p.add(new BaseXLabel(SEPARATOR, true, true));
    p.add(sep);

    p.add(new BaseXLabel(FORMAT + COL, true, true));
    final CsvFormat[] formats = CsvFormat.values();
    final int fl = formats.length - 1;
    final StringList frmts = new StringList(fl);
    for(int f = 0; f < fl; f++) frmts.add(formats[f].toString());
    format = new BaseXCombo(d, frmts.finish());
    format.setSelectedItem(copts.get(CsvOptions.FORMAT));
    p.add(format);
    pp.add(p);

    p = new BaseXBack(new TableLayout(4, 1));
    header = new BaseXCheckBox(FIRST_LINE_HEADER, CsvOptions.HEADER, copts, d);
    p.add(header);
    quotes = new BaseXCheckBox(PARSE_QUOTES, CsvOptions.QUOTES, copts, d);
    p.add(quotes);
    backslashes = new BaseXCheckBox(BACKSLASHES, CsvOptions.BACKSLASHES, copts, d);
    p.add(backslashes);
    lax = new BaseXCheckBox(LAX_NAME_CONVERSION, CsvOptions.LAX, copts, d);
    p.add(lax);

    pp.add(p);

    add(pp, BorderLayout.WEST);

    example = new TextPanel(false, d);

    add(example, BorderLayout.CENTER);
    action(true);
  }

  @Override
  boolean action(final boolean active) {
    try {
      final boolean head = header.isSelected();
      format.setEnabled(head);
      lax.setEnabled(head && copts.get(CsvOptions.FORMAT) == CsvFormat.DIRECT);

      example.setText(example(MainParser.CSV.name(), EXAMPLE,
          CsvConverter.get(copts).convert(new IOContent(EXAMPLE)).serialize().toString()));
    } catch(final IOException ex) {
      example.setText(error(ex));
    }

    final boolean fixedsep = seps.getSelectedIndex() < CsvSep.values().length;
    sepchar.setEnabled(!fixedsep);
    if(fixedsep) sepchar.setText(new TokenBuilder().add(copts.separator()).toString());
    return fixedsep || sepchar.getText().length() == 1;
  }

  @Override
  void update() {
    final String enc = encoding.getSelectedItem();
    copts.set(CsvParserOptions.ENCODING, enc.equals(Strings.UTF8) ? null : enc);
    copts.set(CsvOptions.HEADER, header.isSelected());
    copts.set(CsvOptions.FORMAT, format.getSelectedItem());
    copts.set(CsvOptions.LAX, lax.isSelected());
    copts.set(CsvOptions.QUOTES, quotes.isSelected());
    copts.set(CsvOptions.BACKSLASHES, backslashes.isSelected());
    String sep;
    if(seps.getSelectedIndex() < CsvSep.values().length) {
      sep = seps.getSelectedItem();
    } else {
      sep = sepchar.getText();
      for(final CsvSep cs : CsvSep.values()) {
        if(String.valueOf(cs.sep).equals(sep)) sep = cs.toString();
      }
    }
    copts.set(CsvOptions.SEPARATOR, sep);
  }

  @Override
  void setOptions(final GUI gui) {
    gui.set(MainOptions.CSVPARSER, copts);
  }
}
