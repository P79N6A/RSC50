package org.basex.build.text;

import static org.basex.util.Token.REPLACEMENT;
import static org.basex.util.Token.token;

import java.io.IOException;

import org.basex.build.SingleParser;
import org.basex.core.MainOptions;
import org.basex.io.IO;
import org.basex.io.in.NewlineInput;
import org.basex.util.TokenBuilder;
import org.basex.util.XMLToken;

/**
 * This class parses files in the plain-text format
 * and converts them to XML.
 *
 * <p>The parser provides some options, which can be specified via the
 * {@link MainOptions#TEXTPARSER} option.</p>
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class TextParser extends SingleParser {
  /** Text element. */
  private static final byte[] TEXT = token("text");
  /** Line element. */
  private static final byte[] LINE = token("line");

  /** Lines format. */
  private final boolean lines;
  /** Encoding. */
  private final String encoding;

  /**
   * Constructor.
   * @param source document source
   * @param opts database options
   */
  public TextParser(final IO source, final MainOptions opts) {
    super(source, opts);
    final TextOptions topts = opts.get(MainOptions.TEXTPARSER);
    lines = topts.get(TextOptions.LINES);
    encoding = topts.get(TextOptions.ENCODING);
  }

  @Override
  public void parse() throws IOException {
    builder.openElem(TEXT, atts, nsp);

    final TokenBuilder tb = new TokenBuilder();
    try(final NewlineInput nli = new NewlineInput(source).encoding(encoding)) {
      for(int ch; (ch = nli.read()) != -1;) {
        if(ch == '\n' && lines) {
          builder.openElem(LINE, atts, nsp);
          builder.text(tb.next());
          builder.closeElem();
        } else {
          tb.add(XMLToken.valid(ch) ? ch : REPLACEMENT);
        }
      }
    }
    if(!lines) builder.text(tb.finish());
    builder.closeElem();
  }
}
