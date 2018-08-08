package org.basex.io.serial;

import static org.basex.data.DataText.V10;
import static org.basex.data.DataText.V11;
import static org.basex.query.QueryError.SERDT;
import static org.basex.query.QueryError.SERSA;

import java.io.IOException;
import java.io.OutputStream;

import org.basex.query.QueryIOException;
import org.basex.query.util.ft.FTPos;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.QNm;

/**
 * This class serializes items as XML.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class XMLSerializer extends MarkupSerializer {
  /** Indicates if root element has been serialized. */
  private boolean root;

  /**
   * Constructor, specifying serialization options.
   * @param os output stream
   * @param sopts serialization parameters
   * @throws IOException I/O exception
   */
  XMLSerializer(final OutputStream os, final SerializerOptions sopts) throws IOException {
    super(os, sopts, V10, V11);
  }

  @Override
  protected void startOpen(final QNm name) throws IOException {
    if(elems.isEmpty()) {
      if(root) check();
      root = true;
    }
    super.startOpen(name);
  }

  @Override
  protected void text(final byte[] value, final FTPos ftp) throws IOException {
    if(elems.isEmpty()) check();
    super.text(value, ftp);
  }

  @Override
  protected void atomic(final Item it) throws IOException {
    if(elems.isEmpty()) check();
    super.atomic(it);
  }

  @Override
  protected void doctype(final QNm type) throws IOException {
    if(docsys != null) printDoctype(type, docpub, docsys);
  }

  /**
   * Checks if document serialization is valid.
   * @throws QueryIOException query I/O exception
   */
  private void check() throws QueryIOException {
    if(!saomit) throw SERSA.getIO();
    if(docsys != null) throw SERDT.getIO();
  }
}
