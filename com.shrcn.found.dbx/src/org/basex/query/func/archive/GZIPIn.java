package org.basex.query.func.archive;

import static org.basex.query.func.archive.ArchiveText.GZIP;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;

import org.basex.util.Util;

/**
 * GZIP reader.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class GZIPIn extends ArchiveIn {
  /** GZIP input stream. */
  private final GZIPInputStream zis;
  /** Flag. */
  private boolean more;

  /**
   * Constructor.
   * @param is input stream
   * @throws IOException I/O exception
   */
  GZIPIn(final InputStream is) throws IOException {
    zis = new GZIPInputStream(is);
  }

  @Override
  public boolean more() {
    return more ^= true;
  }

  @Override
  public ZipEntry entry() {
    final ZipEntry ze = new ZipEntry("");
    ze.setMethod(ZipEntry.DEFLATED);
    return ze;
  }

  @Override
  public int read(final byte[] d) throws IOException {
    return zis.read(d);
  }

  @Override
  public String format() {
    return GZIP;
  }

  @Override
  public void close() {
    try { zis.close(); } catch(final IOException ex) { Util.debug(ex); }
  }
}
