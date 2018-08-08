package org.basex.core.cmd;

import static org.basex.core.Text.CREATE_STATS_D;
import static org.basex.core.Text.DB_NOT_DROPPED_X;
import static org.basex.core.Text.DB_NOT_RENAMED_X;
import static org.basex.core.Text.DB_OPTIMIZED_X;
import static org.basex.core.Text.DB_PINNED_X;
import static org.basex.core.Text.NO_MAINMEM;
import static org.basex.core.Text.S_ALL;

import java.io.IOException;

import org.basex.build.Builder;
import org.basex.build.DiskBuilder;
import org.basex.build.Parser;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.MainOptions;
import org.basex.core.StaticOptions;
import org.basex.core.locks.DBLocking;
import org.basex.core.locks.LockResult;
import org.basex.core.parse.CmdBuilder;
import org.basex.core.parse.Commands.Cmd;
import org.basex.core.users.Perm;
import org.basex.data.Data;
import org.basex.data.DiskData;
import org.basex.data.MetaData;
import org.basex.io.IO;
import org.basex.io.IOFile;
import org.basex.io.serial.BuilderSerializer;
import org.basex.io.serial.Serializer;
import org.basex.query.value.item.QNm;
import org.basex.query.value.node.DBNode;
import org.basex.util.Util;
import org.basex.util.list.IntList;

/**
 * Evaluates the 'optimize all' command and rebuilds all data structures of
 * the currently opened database. This effectively eliminates all fragmentation
 * and can lead to significant space savings after updates.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public final class OptimizeAll extends ACreate {
  /** Current pre value. */
  private int pre;
  /** Data size. */
  private int size;

  /**
   * Default constructor.
   */
  public OptimizeAll() {
    super(Perm.WRITE, true);
  }

  @Override
  protected boolean run() {
    final Data data = context.data();
    if(!startUpdate(data)) return false;
    boolean ok = true;
    try {
      optimizeAll(data, context, options, this);
    } catch(final IOException ex) {
      ok = error(Util.message(ex));
    } finally {
      context.closeDB();
      ok &= finishUpdate(data);
    }
    if(!ok) return false;

    final Open open = new Open(data.meta.name);
    return open.run(context) ? info(DB_OPTIMIZED_X, data.meta.name, job().performance) :
      error(open.info());
  }

  @Override
  public boolean newData(final Context ctx) {
    return true;
  }

  @Override
  public void databases(final LockResult lr) {
    lr.write.add(DBLocking.CONTEXT);
  }

  @Override
  public double progressInfo() {
    return (double) pre / size;
  }

  @Override
  public boolean stoppable() {
    return false;
  }

  @Override
  public String detailedInfo() {
    return CREATE_STATS_D;
  }

  @Override
  public void build(final CmdBuilder cb) {
    cb.init(Cmd.OPTIMIZE + " " + S_ALL);
  }

  /**
   * Optimizes all data structures and closes the database.
   * Recreates the database, drops the old instance and renames the recreated instance.
   * @param data disk data
   * @param context database context
   * @param options main options
   * @param cmd command reference or {@code null}
   * @throws IOException I/O Exception during index rebuild
   * @throws BaseXException database exception
   */
  public static void optimizeAll(final Data data, final Context context,
      final MainOptions options, final OptimizeAll cmd) throws IOException {

    if(data.inMemory()) throw new BaseXException(NO_MAINMEM);

    final DiskData odata = (DiskData) data;
    final MetaData ometa = odata.meta;

    // check if database is also pinned by other users
    final String name = ometa.name;
    if(context.datas.pins(name) > 1) throw new BaseXException(DB_PINNED_X, name);

    // adopt original index options
    options.set(MainOptions.TEXTINDEX, ometa.textindex);
    options.set(MainOptions.ATTRINDEX, ometa.attrindex);
    options.set(MainOptions.TOKENINDEX, ometa.tokenindex);
    options.set(MainOptions.FTINDEX, ometa.ftindex);
    options.set(MainOptions.TEXTINCLUDE, ometa.textinclude);
    options.set(MainOptions.ATTRINCLUDE, ometa.attrinclude);
    options.set(MainOptions.TOKENINCLUDE, ometa.tokeninclude);
    options.set(MainOptions.FTINCLUDE, ometa.ftinclude);
    // adopt original full-text index options
    options.set(MainOptions.STEMMING, ometa.stemming);
    options.set(MainOptions.CASESENS, ometa.casesens);
    options.set(MainOptions.DIACRITICS, ometa.diacritics);
    options.set(MainOptions.LANGUAGE, ometa.language.toString());
    options.set(MainOptions.STOPWORDS, ometa.stopwords);
    // adopt original index options
    options.set(MainOptions.MAXLEN, ometa.maxlen);
    options.set(MainOptions.MAXCATS, ometa.maxcats);

    // build database and index structures
    if(cmd != null) cmd.size = ometa.size;
    final StaticOptions sopts = context.soptions;
    final String tmpName = sopts.randomDbName(name);
    final DBParser parser = new DBParser(odata, options, cmd);
    try(final DiskBuilder builder = new DiskBuilder(tmpName, parser, sopts, options)) {
      final DiskData ndata = builder.build();
      final MetaData nmeta = ndata.meta;
      try {
        // adopt original meta data, create new index structures
        nmeta.createtext = ometa.createtext;
        nmeta.createattr = ometa.createattr;
        nmeta.createtoken = ometa.createtoken;
        nmeta.createft = ometa.createft;
        nmeta.original = ometa.original;
        nmeta.filesize = ometa.filesize;
        nmeta.time = ometa.time;
        nmeta.dirty = true;
        CreateIndex.create(ndata, cmd);

        // move binary files
        final IOFile bin = ometa.binaries();
        if(bin.exists()) bin.rename(nmeta.binaries());
      } finally {
        ndata.close();
      }
    }

    // close old database instance, drop it and rename temporary database
    Close.close(odata, context);
    if(!DropDB.drop(name, sopts)) throw new BaseXException(DB_NOT_DROPPED_X, name);
    if(!AlterDB.alter(tmpName, name, sopts)) throw new BaseXException(DB_NOT_RENAMED_X, tmpName);
  }

  /**
   * Parser for rebuilding existing databases.
   *
   * @author BaseX Team 2005-16, BSD License
   * @author Leo Woerteler
   */
  private static final class DBParser extends Parser {
    /** Disk data. */
    private final DiskData data;
    /** Calling command (may be {@code null}). */
    final OptimizeAll cmd;

    /**
     * Constructor.
     * @param data disk data
     * @param options main options
     * @param cmd calling command (may be {@code null})
     */
    DBParser(final DiskData data, final MainOptions options, final OptimizeAll cmd) {
      super(data.meta.original.isEmpty() ? null : IO.get(data.meta.original), options);
      this.data = data;
      this.cmd = cmd;
    }

    @Override
    public void parse(final Builder build) throws IOException {
      final Serializer ser = new BuilderSerializer(build) {
        @Override
        protected void startOpen(final QNm name) throws IOException {
          super.startOpen(name);
          if(cmd != null) cmd.pre++;
        }

        @Override
        protected void openDoc(final byte[] name) throws IOException {
          super.openDoc(name);
          if(cmd != null) cmd.pre++;
        }
      };

      final IntList il = data.resources.docs();
      final int is = il.size();
      for(int i = 0; i < is; i++) ser.serialize(new DBNode(data, il.get(i)));
    }
  }
}
