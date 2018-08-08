package org.basex.data;

import static org.basex.core.Text.NO_MAINMEM;

import java.io.IOException;

import org.basex.core.BaseXException;
import org.basex.core.Command;
import org.basex.core.MainOptions;
import org.basex.index.IdPreMap;
import org.basex.index.IndexBuilder;
import org.basex.index.IndexType;
import org.basex.index.name.Names;
import org.basex.index.path.PathIndex;
import org.basex.index.value.MemValuesBuilder;
import org.basex.index.value.ValueIndex;
import org.basex.io.random.TableMemAccess;
import org.basex.util.Token;
import org.basex.util.Util;
import org.basex.util.hash.TokenSet;

/**
 * This class stores and organizes the database table and the index structures
 * for textual content in a compressed memory structure.
 * The table mapping is documented in {@link Data}.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class MemData extends Data {
  /** Texts. */
  private final TokenSet texts;
  /** Attribute Values. */
  private final TokenSet values;

  /**
   * Constructor.
   * @param paths path index
   * @param nspaces namespaces
   * @param opts database options
   */
  public MemData(final PathIndex paths, final Namespaces nspaces, final MainOptions opts) {
    this(null, null, paths, nspaces, null, null, opts);
  }

  /**
   * Constructor for creating a new, empty database.
   * @param opts database options
   */
  public MemData(final MainOptions opts) {
    this(null, null, opts);
  }

  /**
   * Constructor for building a new database.
   * @param elemNames element name index
   * @param attrNames attribute name index
   * @param paths path index
   * @param nspaces namespaces
   * @param texts texts
   * @param values values
   * @param options database options
   */
  private MemData(final Names elemNames, final Names attrNames, final PathIndex paths,
      final Namespaces nspaces, final TokenSet texts, final TokenSet values,
      final MainOptions options) {

    super(new MetaData(options));
    table = new TableMemAccess(meta);
    if(meta.updindex) idmap = new IdPreMap(meta.lastid);
    this.texts = texts == null ? new TokenSet() : texts;
    this.values = values == null ? new TokenSet() : values;
    this.elemNames = elemNames == null ? new Names(meta) : elemNames;
    this.attrNames = attrNames == null ? new Names(meta) : attrNames;
    this.paths = paths == null ? new PathIndex(this) : paths;
    this.nspaces = nspaces == null ? new Namespaces() : nspaces;
  }

  @Override
  public void createIndex(final IndexType type, final Command cmd) throws IOException {
    final IndexBuilder ib;
    switch(type) {
      case TEXT: case ATTRIBUTE: case TOKEN:
        ib = new MemValuesBuilder(this, type); break;
      case FULLTEXT:
        throw new BaseXException(NO_MAINMEM);
      default:
        throw Util.notExpected();
    }
    try {
      if(cmd != null) cmd.pushJob(ib);
      set(type, ib.build());
    } finally {
      if(cmd != null) cmd.popJob();
    }
  }

  @Override
  public void dropIndex(final IndexType type) throws BaseXException {
    switch(type) {
      case TEXT:      break;
      case ATTRIBUTE: break;
      case TOKEN:     break;
      case FULLTEXT:  throw new BaseXException(NO_MAINMEM);
      default:        throw Util.notExpected();
    }
    set(type, null);
  }

  /**
   * Assigns the specified index.
   * @param type index to be opened
   * @param index index instance
   */
  private void set(final IndexType type, final ValueIndex index) {
    meta.dirty = true;
    switch(type) {
      case TEXT:      textIndex = index; break;
      case ATTRIBUTE: attrIndex = index; break;
      case TOKEN:     tokenIndex = index; break;
      case FULLTEXT:  ftIndex = index; break;
      default:        break;
    }
  }

  @Override
  public void startUpdate(final MainOptions opts) { }

  @Override
  public void finishUpdate(final MainOptions opts) { }

  @Override
  public void flush(final boolean all) { }

  @Override
  public byte[] text(final int pre, final boolean text) {
    return (text ? texts : values).key((int) textRef(pre));
  }

  @Override
  public long textItr(final int pre, final boolean text) {
    return Token.toLong(text(pre, text));
  }

  @Override
  public double textDbl(final int pre, final boolean text) {
    return Token.toDouble(text(pre, text));
  }

  @Override
  public int textLen(final int pre, final boolean text) {
    return text(pre, text).length;
  }

  @Override
  public boolean inMemory() {
    return true;
  }

  /**
   * Returns the string values of the database.
   * @param text text/attribute flag
   * @return set
   */
  public TokenSet values(final boolean text) {
    return text ? texts : values;
  }

  // UPDATE OPERATIONS ========================================================

  @Override
  protected void delete(final int pre, final boolean text) { }

  @Override
  protected void updateText(final int pre, final byte[] value, final int kind) {
    indexDelete(pre, -1, 1);
    textRef(pre, textRef(value, kind != ATTR));
    indexAdd(pre, -1, 1, null);
  }

  @Override
  protected long textRef(final byte[] value, final boolean text) {
    return (text ? texts : values).put(value);
  }
}
