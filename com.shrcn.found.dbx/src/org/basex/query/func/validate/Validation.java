package org.basex.query.func.validate;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.basex.io.IO;
import org.basex.io.IOContent;
import org.basex.io.IOFile;
import org.basex.io.IOStream;
import org.basex.query.QueryException;
import org.basex.util.Prop;
import org.xml.sax.SAXException;

/** Abstract validator class. */
abstract class Validation {
  /** Temporary schema instance. */
  private IOFile schema;

  /**
   * Starts the validation.
   * @param h error handler
   * @throws IOException I/O exception
   * @throws ParserConfigurationException parser configuration exception
   * @throws SAXException SAX exception
   * @throws QueryException query exception
   */
  abstract void process(ErrorHandler h)
      throws IOException, ParserConfigurationException, SAXException, QueryException;

  /**
   * Prepares validation. Creates a temporary file from the specified IO reference if it is
   * main-memory content or a streaming reference.
   * @param in schema file
   * @param handler error handler
   * @return resulting file
   * @throws IOException I/O exception
   */
  protected IO prepare(final IO in, final ErrorHandler handler) throws IOException {
    if(in instanceof IOContent || in instanceof IOStream) {
      // cache main-memory content or stream to file
      schema = new IOFile(File.createTempFile(Prop.NAME + '-', IO.TMPSUFFIX));
      schema.write(in.read());
      handler.schema(schema);
      return schema;
    }
    return in;
  }

  /**
   * Closes a temporary schema instance.
   */
  void finish() {
    if(schema != null) schema.delete();
  }
}
