package org.basex.io.out;

import static org.basex.query.QueryError.SERENC_X_X;
import static org.basex.util.Token.string;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.UnmappableCharacterException;

import org.basex.util.TokenBuilder;

/**
 * This class is a wrapper for outputting texts with specific encodings.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class EncoderOutput extends PrintOutput {
  /** Encoding. */
  private final Charset encoding;
  /** Charset encoder. */
  private final CharsetEncoder encoder;
  /** Encoding buffer. */
  private final TokenBuilder encbuffer;

  /**
   * Constructor, given an output stream.
   * @param os output stream reference
   * @param encoding encoding
   */
  public EncoderOutput(final OutputStream os, final Charset encoding) {
    super(os);
    this.encoding = encoding;
    encoder = encoding.newEncoder();
    encbuffer = new TokenBuilder();
  }

  @Override
  public void print(final int ch) throws IOException {
    encbuffer.reset();
    encoder.reset();
    try {
      final ByteBuffer bb = encoder.encode(CharBuffer.wrap(encbuffer.add(ch).toString()));
      write(bb.array(), 0, bb.limit());
    } catch(final UnmappableCharacterException ex) {
      throw SERENC_X_X.getIO(Integer.toHexString(ch), encoding);
    }
  }

  @Override
  public void print(final byte[] token) throws IOException {
    print(string(token));
  }

  @Override
  public void print(final String string) throws IOException {
    write(string.getBytes(encoding));
  }
}
