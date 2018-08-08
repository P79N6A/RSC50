package org.basex.io.serial;

import java.io.IOException;
import java.io.OutputStream;

import org.basex.query.util.ft.FTPos;

/**
 * This class serializes items as text.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class TextSerializer extends StandardSerializer {
  /**
   * Constructor, specifying serialization options.
   * @param os output stream
   * @param sopts serialization parameters
   * @throws IOException I/O exception
   */
  TextSerializer(final OutputStream os, final SerializerOptions sopts) throws IOException {
    super(os, sopts);
  }

  @Override
  protected void text(final byte[] value, final FTPos ftp) throws IOException {
    out.print(norm(value));
    sep = false;
  }
}
