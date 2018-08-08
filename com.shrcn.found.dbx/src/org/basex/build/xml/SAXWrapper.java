package org.basex.build.xml;

import static org.basex.core.Text.COLS;
import static org.basex.core.Text.SCANPOS_X_X;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.basex.build.SingleParser;
import org.basex.core.MainOptions;
import org.basex.core.jobs.JobException;
import org.basex.io.IO;
import org.basex.io.IOContent;
import org.basex.io.IOFile;
import org.basex.io.IOUrl;
import org.basex.util.Util;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 * This class parses an XML document with Java's internal SAX parser. Note that
 * not all files cannot be parsed with the default parser; for example, the
 * DBLP documents contain too many entities and cause an out of memory error.
 * The internal {@link XMLParser} can be used as alternative.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class SAXWrapper extends SingleParser {
  /** File counter. */
  private long counter;
  /** Current line. */
  private int line = 1;

  /** SAX handler reference. */
  private SAXHandler saxh;
  /** Optional XML reader. */
  private final SAXSource saxs;
  /** File length. */
  private long length;

  /**
   * Constructor.
   * @param source sax source
   * @param opts database options
   */
  public SAXWrapper(final IO source, final MainOptions opts) {
    super(source, opts);
    saxs = new SAXSource(source.inputSource());
  }

  @Override
  public void parse() throws IOException {
    final InputSource is = wrap(saxs.getInputSource());
    try {
      XMLReader reader = saxs.getXMLReader();
      if(reader == null) {
        final boolean dtd = options.get(MainOptions.DTD);
        final SAXParserFactory f = SAXParserFactory.newInstance();
        f.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", dtd);
        f.setFeature("http://xml.org/sax/features/external-parameter-entities", dtd);
        f.setFeature("http://xml.org/sax/features/use-entity-resolver2", false);

        f.setNamespaceAware(true);
        f.setValidating(false);
        f.setXIncludeAware(options.get(MainOptions.XINCLUDE));
        reader = f.newSAXParser().getXMLReader();
      }

      saxh = new SAXHandler(builder, options.get(MainOptions.CHOP),
          options.get(MainOptions.STRIPNS));
      final String path = options.get(MainOptions.CATFILE);
      if(!path.isEmpty()) CatalogWrapper.set(reader, path);

      reader.setDTDHandler(saxh);
      reader.setContentHandler(saxh);
      reader.setProperty("http://xml.org/sax/properties/lexical-handler", saxh);
      reader.setErrorHandler(saxh);

      if(is != null) reader.parse(is);
      else reader.parse(saxs.getSystemId());
    } catch(final SAXParseException ex) {
      final String msg = Util.info(SCANPOS_X_X, source, ex.getLineNumber(),
          ex.getColumnNumber()) + COLS + Util.message(ex);
      throw new IOException(msg, ex);
    } catch(final JobException ex) {
      throw ex;
    } catch(final Exception ex) {
      // occurs, e.g. if document encoding is invalid:
      // prefix message with source id
      final String msg = "\"" + source + '"' + COLS + Util.message(ex);
      // wrap and return original message
      throw new IOException(msg, ex);
    } finally {
      if(is != null) {
        try(final Reader r = is.getCharacterStream()) { }
        try(final InputStream ist = is.getByteStream()) { }
      }
    }
  }

  /**
   * Wraps the input source with a stream which counts the number of read bytes
   * and parsed lines.
   * @param is input source
   * @return resulting stream
   * @throws IOException I/O exception
   */
  @SuppressWarnings("resource")
  private InputSource wrap(final InputSource is) throws IOException {
    if(is == null) return null;

    // choose input stream
    final InputStream in;
    if(is.getByteStream() != null) {
      in = is.getByteStream();
    } else if(is.getSystemId() == null || is.getSystemId().isEmpty()) {
      return is;
    } else if(source instanceof IOFile) {
      in = new FileInputStream(source.path());
    } else if(source instanceof IOContent || source instanceof IOUrl) {
      in = new ByteArrayInputStream(source.read());
    } else {
      return is;
    }

    // retrieve/estimate number of bytes to be read
    length = source.length();
    try {
      if(length <= 0) length = in.available();
    } catch(final IOException ex) {
      in.close();
      throw ex;
    }

    // create wrapper
    final InputSource tmp = new InputSource(new InputStream() {
      final InputStream buffer = in instanceof ByteArrayInputStream ? in :
        new BufferedInputStream(in);

      @Override
      public int read() throws IOException {
        final int i = buffer.read();
        if(i == '\n') ++line;
        ++counter;
        return i;
      }

      @Override
      public void close() throws IOException {
        buffer.close();
      }
    });

    saxs.setInputSource(tmp);
    saxs.setSystemId(is.getSystemId());
    return tmp;
  }

  @Override
  public String detailedInfo() {
    return length == 0 ? super.detailedInfo() : Util.info(SCANPOS_X_X, source.name(), line);
  }

  @Override
  public double progressInfo() {
    return length == 0 ? saxh == null ? 0 : saxh.nodes / 3000000d % 1 :
      (double) counter / length;
  }
}
