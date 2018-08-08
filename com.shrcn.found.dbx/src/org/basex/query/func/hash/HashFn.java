package org.basex.query.func.hash;

import static org.basex.query.QueryError.FILE_IO_ERROR_X;
import static org.basex.query.QueryError.HASH_ALG_X;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.basex.io.IO;
import org.basex.io.in.BufferInput;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.value.item.B64;
import org.basex.query.value.item.B64Stream;
import org.basex.query.value.item.Item;

/**
 * Hashing function.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
abstract class HashFn extends StandardFunc {
  /**
   * Creates the hash of a string, using the given algorithm.
   * @param algo hashing algorithm
   * @param qc query context
   * @return xs:hexBinary instance containing the hash
   * @throws QueryException exception
   */
  final B64 hash(final String algo, final QueryContext qc) throws QueryException {
    final Item item = exprs[0].atomItem(qc, info);
    try {
      final MessageDigest md = MessageDigest.getInstance(algo);
      if(item instanceof B64Stream) {
        try(final BufferInput bi = item.input(info)) {
          final byte[] tmp = new byte[IO.BLOCKSIZE];
          do {
            final int n = bi.read(tmp);
            if(n == -1) return new B64(md.digest());
            md.update(tmp, 0, n);
          } while(true);
        } catch(final IOException ex) {
          throw FILE_IO_ERROR_X.get(info, ex);
        }
      }
      // non-streaming item, string
      return new B64(md.digest(toBytes(item)));
    } catch(final NoSuchAlgorithmException ex) {
      throw HASH_ALG_X.get(info, algo);
    }
  }
}
