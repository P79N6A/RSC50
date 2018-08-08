package org.basex.query.value.item;

import java.io.IOException;

import org.basex.io.IO;
import org.basex.io.in.BufferInput;
import org.basex.io.in.TextInput;
import org.basex.query.QueryError;
import org.basex.query.QueryException;
import org.basex.query.func.Function;
import org.basex.util.InputInfo;
import org.basex.util.Token;
import org.basex.util.Util;

/**
 * Streamable string item ({@code xs:string}).
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class StrStream extends AStr implements Streamable {
  /** Input reference. */
  private final IO input;
  /** Encoding (optional). */
  private final String encoding;
  /** Error message. */
  private final QueryError error;
  /** Validation flag. */
  private final boolean validate;

  /**
   * Constructor.
   * @param input input
   * @param encoding encoding (may be null)
   * @param validate validate flag
   * @param error error message to be thrown
   */
  public StrStream(final IO input, final String encoding, final QueryError error,
      final boolean validate) {
    this.input = input;
    this.encoding = encoding;
    this.error = error;
    this.validate = validate;
  }

  @Override
  public byte[] string(final InputInfo ii) throws QueryException {
    materialize(ii);
    return value;
  }

  @Override
  public String toJava() throws QueryException {
    return Token.string(string(null));
  }

  @Override
  public BufferInput input(final InputInfo ii) throws QueryException {
    if(value != null) return super.input(ii);

    TextInput ti = null;
    try {
      ti = new TextInput(input);
      ti.encoding(encoding).validate(validate);
      return ti;
    } catch(final IOException ex) {
      if(ti != null) try { ti.close(); } catch(final IOException ignore) { }
      throw error.get(ii, ex);
    }
  }

  @Override
  public void materialize(final InputInfo ii) throws QueryException {
    try {
      if(value == null) value = input(ii).content();
    } catch(final IOException ex) {
      throw error.get(ii, ex);
    }
  }

  @Override
  public String toString() {
    try {
      return toJava();
    } catch(final QueryException ex) {
      return Util.info(Function._FILE_READ_TEXT.args(input));
    }
  }
}
