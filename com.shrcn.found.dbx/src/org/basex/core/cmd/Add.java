package org.basex.core.cmd;

import static org.basex.core.Text.ADD;
import static org.basex.core.Text.NAME_INVALID_X;
import static org.basex.core.Text.PATH_INVALID_X;
import static org.basex.core.Text.RES_ADDED_X;
import static org.basex.core.Text.RES_NOT_FOUND;
import static org.basex.core.Text.RES_NOT_FOUND_X;
import static org.basex.core.Text.S_TO;

import java.io.IOException;
import java.io.InputStream;

import org.basex.build.Builder;
import org.basex.build.DirParser;
import org.basex.build.DiskBuilder;
import org.basex.build.MemBuilder;
import org.basex.build.Parser;
import org.basex.core.MainOptions;
import org.basex.core.parse.CmdBuilder;
import org.basex.core.users.Perm;
import org.basex.data.Data;
import org.basex.data.DataClip;
import org.basex.data.MetaData;
import org.basex.io.IO;
import org.basex.io.IOFile;
import org.basex.query.up.atomic.AtomicUpdateCache;
import org.basex.util.Performance;
import org.basex.util.Util;

/**
 * Evaluates the 'add' command and adds a document to a collection.<br/>
 * Note that the constructors of this class have changed with Version 7.0:
 * the target path and file name have been merged and are now specified
 * as first argument.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Add extends ACreate {
  /** Builder. */
  private Builder build;

  /** Data clip to insert. */
  DataClip clip;

  /**
   * Constructor, specifying a target path.
   * The input needs to be set via {@link #setInput(InputStream)}.
   * @param path target path, optionally terminated by a new file name
   */
  public Add(final String path) {
    this(path, null);
  }

  /**
   * Constructor, specifying a target path and an input.
   * @param path target path, optionally terminated by a new file name.
   * If {@code null}, the name of the input will be set as path.
   * @param input input file or XML string
   */
  public Add(final String path, final String input) {
    super(Perm.WRITE, true, path == null ? "" : path, input);
  }

  @Override
  protected boolean run() {
    try {
      if(!build()) return false;

      // skip update if fragment is empty
      final Data data = context.data();
      if(clip.data.meta.size > 1) {
        if(!startUpdate(data)) return false;

        context.invalidate();
        final AtomicUpdateCache auc = new AtomicUpdateCache(data);
        auc.addInsert(data.meta.size, -1, clip);
        auc.execute(false);

        if(!finishUpdate(data)) return false;
      }
      return info(RES_ADDED_X, job().performance);
    } finally {
      finish();
    }
  }

  /**
   * Builds a data clip for the document(s) to be added.
   * @return success flag
   */
  boolean build() {
    String name = MetaData.normPath(args[0]);
    if(name == null) return error(PATH_INVALID_X, args[0]);

    // retrieve input
    final IO io;
    try {
      io = sourceToIO(name);
    } catch(final IOException ex) {
      return error(Util.message(ex));
    }

    // check if resource exists
    if(io == null) return error(RES_NOT_FOUND);
    if(!io.exists()) return in != null ? error(RES_NOT_FOUND) :
        error(RES_NOT_FOUND_X, context.user().has(Perm.CREATE) ? io : args[1]);

    if(!name.endsWith("/") && (io.isDir() || io.isArchive())) name += '/';

    String target = "";
    final int s = name.lastIndexOf('/');
    if(s != -1) {
      target = name.substring(0, s);
      name = name.substring(s + 1);
    }

    // get name from io reference
    if(name.isEmpty()) name = io.name();
    else io.name(name);

    // ensure that the final name is not empty
    if(name.isEmpty()) return error(NAME_INVALID_X, name);

    try {
      final Data data = context.data();
      final Parser parser = new DirParser(io, options, data.meta.path);
      parser.target(target);

      // create random database name for disk-based creation
      if(cache(parser)) {
        build = new DiskBuilder(soptions.randomDbName(data.meta.name), parser, soptions, options);
      } else {
        build = new MemBuilder(name, parser);
      }
      clip = build.dataClip();
      return true;
    } catch(final IOException ex) {
      return error(Util.message(ex));
    }
  }

  /**
   * Finalizes an add operation.
   */
  void finish() {
    if(clip != null) DropDB.drop(clip.data, soptions);
  }

  /**
   * Decides if the input should be cached before being written to the final database.
   * @param parser parser reference
   * @return result of check
   */
  private boolean cache(final Parser parser) {
    // main memory mode: never write to disk
    if(options.get(MainOptions.MAINMEM)) return false;
    // explicit caching
    if(options.get(MainOptions.ADDCACHE)) return true;

    // create disk instances for large documents
    // (does not work for input streams and directories)
    long fl = parser.source.length();
    if(parser.source instanceof IOFile) {
      final IOFile f = (IOFile) parser.source;
      if(f.isDir()) {
        for(final String d : f.descendants()) fl += new IOFile(f, d).length();
      }
    }

    // check free memory
    final Runtime rt = Runtime.getRuntime();
    final long max = rt.maxMemory();
    if(fl < (max - rt.freeMemory()) / 2) return false;
    // if caching may be necessary, run garbage collection and try again
    Performance.gc(2);
    return fl > (max - rt.freeMemory()) / 2;
  }

  @Override
  public void build(final CmdBuilder cb) {
    cb.init().arg(S_TO, 0).arg(1);
  }

  @Override
  public String shortInfo() {
    return ADD;
  }

  @Override
  public double progressInfo() {
    return build != null ? build.progressInfo() : 0;
  }
}
