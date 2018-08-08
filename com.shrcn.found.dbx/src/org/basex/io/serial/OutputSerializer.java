package org.basex.io.serial;

import static org.basex.io.serial.SerializerOptions.ENCODING;
import static org.basex.io.serial.SerializerOptions.INDENT;
import static org.basex.io.serial.SerializerOptions.INDENTS;
import static org.basex.io.serial.SerializerOptions.ITEM_SEPARATOR;
import static org.basex.io.serial.SerializerOptions.LIMIT;
import static org.basex.io.serial.SerializerOptions.NEWLINE;
import static org.basex.io.serial.SerializerOptions.TABULATOR;
import static org.basex.query.QueryError.SERENCODING_X;
import static org.basex.util.Token.HEX;
import static org.basex.util.Token.cl;
import static org.basex.util.Token.cp;
import static org.basex.util.Token.token;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.basex.io.out.EncoderOutput;
import org.basex.io.out.NewlineOutput;
import org.basex.io.out.PrintOutput;
import org.basex.query.QueryIOException;
import org.basex.util.Strings;

/**
 * This class serializes items to an output stream.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public abstract class OutputSerializer extends Serializer {
  /** Output stream. */
  protected final PrintOutput out;
  /** Serializer options. */
  protected final SerializerOptions sopts;
  /** Encoding. */
  protected final String encoding;
  /** Item separator. */
  protected byte[] itemsep;

  /** Number of spaces to indent. */
  private final int indents;
  /** Tabular character. */
  private final char tab;

  /**
   * Constructor.
   * @param os output stream
   * @param sopts serializer options
   * @throws QueryIOException query I/O exception
   */
  protected OutputSerializer(final OutputStream os, final SerializerOptions sopts)
      throws QueryIOException {

    this.sopts = sopts;
    indent = sopts.yes(INDENT);

    // project-specific options
    indents = sopts.get(INDENTS);
    tab = sopts.yes(TABULATOR) ? '\t' : ' ';

    encoding = Strings.normEncoding(sopts.get(ENCODING), true);
    PrintOutput po;
    if(encoding == Strings.UTF8) {
      po = PrintOutput.get(os);
    } else {
      try {
        po = new EncoderOutput(os, Charset.forName(encoding));
      } catch(final Exception ex) {
        throw SERENCODING_X.getIO(encoding);
      }
    }
    final int limit = sopts.get(LIMIT);
    if(limit != -1) po.setLimit(limit);

    final byte[] nl = token(sopts.get(NEWLINE).newline());
    if(nl.length != 1 || nl[0] != '\n') po = new NewlineOutput(po, nl);
    out = po;
  }

  @Override
  public void reset() {
    more = false;
  }

  @Override
  public final boolean finished() {
    return out.finished();
  }

  @Override
  public void close() throws IOException {
    out.flush();
  }

  /**
   * Indents the next text.
   * @throws IOException I/O exception
   */
  protected void indent() throws IOException {
    if(indent) {
      out.print('\n');
      final int ls = level * indents;
      for(int l = 0; l < ls; l++) out.print(tab);
    }
  }

  /**
   * Encodes the specified characters before printing.
   * @param text characters to be encoded and printed
   * @throws IOException I/O exception
   */
  protected final void printChars(final byte[] text) throws IOException {
    final int al = text.length;
    for(int a = 0; a < al; a += cl(text, a)) printChar(cp(text, a));
  }

  /**
   * Encodes the specified codepoint before printing.
   * @param cp codepoint to be encoded and printed
   * @throws IOException I/O exception
   */
  protected void printChar(final int cp) throws IOException {
    out.print(cp);
  }

  /**
   * Sets the item separator.
   * @param def default separator
   */
  protected final void itemsep(final String def) {
    final String is = sopts.get(ITEM_SEPARATOR);
    itemsep = sopts.contains(ITEM_SEPARATOR) ? token(is) : def == null ? null : token(def);
  }

  /**
   * Returns a hex entity for the specified codepoint.
   * @param cp codepoint
   * @throws IOException I/O exception
   */
  protected final void printHex(final int cp) throws IOException {
    out.print("&#x");
    boolean o = false;
    for(int i = 3; i >= 0; i--) {
      final int b = (cp >> (i << 3)) & 0xFF;
      if(o || b > 0x0F) {
        out.print(HEX[b >> 4]);
      }
      if(o || b != 0) {
        out.print(HEX[b & 0xF]);
        o = true;
      }
    }
    out.print(';');
  }
}
