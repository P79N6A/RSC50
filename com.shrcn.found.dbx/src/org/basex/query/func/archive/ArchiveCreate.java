package org.basex.query.func.archive;

import static org.basex.query.QueryError.ARCH_DATETIME_X;
import static org.basex.query.QueryError.ARCH_DIFF_X_X;
import static org.basex.query.QueryError.ARCH_EMPTY;
import static org.basex.query.QueryError.ARCH_ENCODING_X;
import static org.basex.query.QueryError.ARCH_FAIL_X;
import static org.basex.query.QueryError.ARCH_LEVEL_X;
import static org.basex.query.QueryError.ARCH_ONE_X;
import static org.basex.query.QueryError.ARCH_SUPP_X_X;
import static org.basex.query.func.archive.ArchiveText.DEFLATE;
import static org.basex.query.func.archive.ArchiveText.ENCODING;
import static org.basex.query.func.archive.ArchiveText.GZIP;
import static org.basex.query.func.archive.ArchiveText.LAST_MOD;
import static org.basex.query.func.archive.ArchiveText.LEVEL;
import static org.basex.query.func.archive.ArchiveText.STORED;
import static org.basex.query.func.archive.ArchiveText.ZIP;
import static org.basex.util.Token.string;
import static org.basex.util.Token.toInt;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.zip.ZipEntry;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.B64;
import org.basex.query.value.item.Bin;
import org.basex.query.value.item.Dtm;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.ANode;
import org.basex.util.InputInfo;
import org.basex.util.Prop;
import org.basex.util.Strings;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class ArchiveCreate extends ArchiveFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Iter entries = qc.iter(exprs[0]), contents = qc.iter(exprs[1]);
    final ArchOptions opts = toOptions(2, new ArchOptions(), qc);

    // check options
    final String format = opts.get(ArchOptions.FORMAT);
    final int level = level(opts);

    try(final ArchiveOut out = ArchiveOut.get(format.toLowerCase(Locale.ENGLISH), info)) {
      out.level(level);
      try {
        int e = 0, c = 0;
        while(true) {
          final Item en = entries.next(), cn = contents.next();
          if(en == null || cn == null) {
            // count remaining entries
            if(cn != null) do c++; while(contents.next() != null);
            if(en != null) do e++; while(entries.next() != null);
            if(e != c) throw ARCH_DIFF_X_X.get(info, e, c);
            break;
          }
          if(out instanceof GZIPOut && c > 0)
            throw ARCH_ONE_X.get(info, format.toUpperCase(Locale.ENGLISH));
          add(checkElemToken(en), cn, out, level, qc);
          e++;
          c++;
        }
      } catch(final IOException ex) {
        throw ARCH_FAIL_X.get(info, ex);
      }
      return new B64(out.finish());
    }
  }

  /**
   * Returns the compression level.
   * @param options options
   * @return level
   * @throws QueryException query exception
   */
  protected int level(final ArchOptions options) throws QueryException {
    int level = ZipEntry.DEFLATED;
    final String format = options.get(ArchOptions.FORMAT);
    final String alg = options.get(ArchOptions.ALGORITHM);
    if(alg != null) {
      if(format.equals(ZIP)  && !Strings.eq(alg, STORED, DEFLATE) ||
         format.equals(GZIP) && !Strings.eq(alg, DEFLATE)) {
        throw ARCH_SUPP_X_X.get(info, ArchOptions.ALGORITHM.name(), alg);
      }
      if(Strings.eq(alg, STORED)) level = ZipEntry.STORED;
      else if(Strings.eq(alg, DEFLATE)) level = ZipEntry.DEFLATED;
    }
    return level;
  }

  /**
   * Adds the specified entry to the output stream.
   * @param entry entry descriptor
   * @param cont contents
   * @param out output archive
   * @param level default compression level
   * @param qc query context
   * @throws QueryException query exception
   * @throws IOException I/O exception
   */
  protected final void add(final Item entry, final Item cont, final ArchiveOut out, final int level,
      final QueryContext qc) throws QueryException, IOException {

    // create new zip entry
    String name = string(entry.string(info));
    if(name.isEmpty()) throw ARCH_EMPTY.get(info);
    if(Prop.WIN) name = name.replace('\\', '/');

    final ZipEntry ze = new ZipEntry(name);
    String enc = Strings.UTF8;

    // compression level
    byte[] lvl = null;
    if(entry instanceof ANode) {
      final ANode el = (ANode) entry;
      lvl = el.attribute(LEVEL);

      // last modified
      final byte[] mod = el.attribute(LAST_MOD);
      if(mod != null) {
        try {
          ze.setTime(dateTimeToMs(new Dtm(mod, info), qc));
        } catch(final QueryException qe) {
          throw ARCH_DATETIME_X.get(info, mod);
        }
      }

      // encoding
      final byte[] ea = el.attribute(ENCODING);
      if(ea != null) {
        enc = Strings.normEncoding(string(ea));
        if(!Charset.isSupported(enc)) throw ARCH_ENCODING_X.get(info, ea);
      }
    }

    // data to be compressed
    byte[] val = toBytes(cont);
    if(!(cont instanceof Bin) && enc != Strings.UTF8) val = encode(val, enc, qc);

    try {
      out.level(lvl == null ? level : toInt(lvl));
    } catch(final IllegalArgumentException ex) {
      throw ARCH_LEVEL_X.get(info, lvl);
    }
    out.write(ze, val);
  }
}
