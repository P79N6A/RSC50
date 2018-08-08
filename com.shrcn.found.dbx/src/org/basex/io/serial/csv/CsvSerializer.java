package org.basex.io.serial.csv;

import static org.basex.query.QueryError.BXCS_SERIAL_X;
import static org.basex.query.QueryError.chop;
import static org.basex.util.Token.EMPTY;
import static org.basex.util.Token.cl;
import static org.basex.util.Token.contains;
import static org.basex.util.Token.cp;

import java.io.IOException;
import java.io.OutputStream;

import org.basex.build.csv.CsvOptions;
import org.basex.io.serial.SerializerOptions;
import org.basex.io.serial.StandardSerializer;
import org.basex.query.value.item.Item;
import org.basex.util.TokenBuilder;
import org.basex.util.Util;
import org.basex.util.list.TokenList;

/**
 * This class serializes items as CSV.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
abstract class CsvSerializer extends StandardSerializer {
  /** CSV options. */
  final CsvOptions copts;
  /** Separator. */
  private final int separator;
  /** Generate quotes. */
  private final boolean quotes;
  /** Generate backslashes. */
  private final boolean backslashes;

  /**
   * Constructor.
   * @param os output stream
   * @param opts serialization parameters
   * @throws IOException I/O exception
   */
  CsvSerializer(final OutputStream os, final SerializerOptions opts) throws IOException {
    super(os, opts);
    copts = opts.get(SerializerOptions.CSV);
    quotes = copts.get(CsvOptions.QUOTES);
    backslashes = copts.get(CsvOptions.BACKSLASHES);
    separator = copts.separator();
  }

  /**
   * Prints a record with the specified fields.
   * @param fields fields to be printed
   * @throws IOException I/O exception
   */
  final void record(final TokenList fields) throws IOException {
    // print fields, skip trailing empty contents
    final int fs = fields.size();
    for(int i = 0; i < fs; i++) {
      final byte[] v = fields.get(i);
      if(i != 0) out.print(separator);

      byte[] txt = v == null ? EMPTY : v;
      final boolean delim = contains(txt, separator) || contains(txt, '\n');
      final boolean special = contains(txt, '\r') || contains(txt, '\t') || contains(txt, '"');
      if(delim || special || backslashes && contains(txt, '\\')) {
        final TokenBuilder tb = new TokenBuilder();
        if(delim && !backslashes && !quotes) throw BXCS_SERIAL_X.getIO(
            Util.info("Output must be put into quotes: %", chop(txt, null)));

        if(quotes && (delim || special)) tb.add('"');
        final int len = txt.length;
        for(int c = 0; c < len; c += cl(txt, c)) {
          int cp = cp(txt, c);
          if(backslashes) {
            if(cp == '\n') tb.add("\\n");
            else if(cp == '\r') tb.add("\\r");
            else if(cp == '\t') tb.add("\\t");
            else if(cp == '"') tb.add("\\\"");
            else if(cp == '\\') tb.add("\\\\");
            else if(cp == separator && !quotes) tb.add('\\').add(cp);
            else tb.add(cp);
          } else {
            if(cp == '"') tb.add('"');
            tb.add(cp);
          }
        }
        if(quotes && (delim || special)) tb.add('"');
        txt = tb.finish();
      }
      out.print(txt);
    }
    out.print('\n');
  }

  @Override
  protected void atomic(final Item value) throws IOException {
    throw BXCS_SERIAL_X.getIO("Atomic values cannot be serialized");
  }
}
