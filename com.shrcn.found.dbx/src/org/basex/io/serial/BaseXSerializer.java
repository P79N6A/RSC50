package org.basex.io.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.basex.query.QueryException;
import org.basex.query.QueryIOException;
import org.basex.query.value.array.Array;
import org.basex.query.value.item.Bin;
import org.basex.query.value.item.Item;
import org.basex.query.value.map.Map;

/**
 * This class serializes items in a project-specific mode.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class BaseXSerializer extends AdaptiveSerializer {
  /** Binary. */
  private final boolean binary;
  /** Level counter. */
  private int count;

  /**
   * Constructor, specifying serialization options.
   * @param os output stream
   * @param sopts serialization parameters
   * @throws IOException I/O exception
   */
  protected BaseXSerializer(final OutputStream os, final SerializerOptions sopts)
      throws IOException {
    super(os, sopts);
    binary = sopts.yes(SerializerOptions.BINARY);
  }

  @Override
  protected void atomic(final Item item) throws IOException {
    if(count == 0) {
      try {
        if(binary && item instanceof Bin) {
          try(final InputStream is = item.input(null)) {
            for(int b; (b = is.read()) != -1;) out.write(b);
          }
        } else {
          printChars(item.string(null));
        }
      } catch(final QueryException ex) {
        throw new QueryIOException(ex);
      }
    } else {
      super.atomic(item);
    }
  }

  @Override
  protected void array(final Array item) throws IOException {
    ++count;
    super.array(item);
    --count;
  }

  @Override
  protected void map(final Map item) throws IOException {
    ++count;
    super.map(item);
    --count;
  }
}
