package org.basex.query.func.file;

import static org.basex.query.QueryError.FILE_IE_ERROR_ACCESS_X;
import static org.basex.query.QueryError.FILE_IO_ERROR_X;
import static org.basex.query.QueryError.FILE_NO_DIR_X;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.value.seq.StrSeq;
import org.basex.util.list.TokenList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FileChildren extends FileRead {
  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    checkCreate(qc);
    try {
      final TokenList children = new TokenList();
      try(DirectoryStream<Path> paths = Files.newDirectoryStream(toPath(0, qc))) {
        for(final Path child : paths) children.add(get(child, Files.isDirectory(child)).string());
      }
      return StrSeq.get(children).iter();
    } catch(final NoSuchFileException | NotDirectoryException ex) {
      throw FILE_NO_DIR_X.get(info, ex);
    } catch(final AccessDeniedException ex) {
      throw FILE_IE_ERROR_ACCESS_X.get(info, ex);
    } catch(final IOException ex) {
      throw FILE_IO_ERROR_X.get(info, ex);
    }
  }
}
