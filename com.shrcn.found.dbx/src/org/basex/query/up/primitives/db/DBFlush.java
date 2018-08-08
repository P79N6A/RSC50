package org.basex.query.up.primitives.db;

import org.basex.core.MainOptions;
import org.basex.data.Data;
import org.basex.query.QueryContext;
import org.basex.query.func.Function;
import org.basex.query.up.primitives.Update;
import org.basex.query.up.primitives.UpdateType;
import org.basex.util.InputInfo;

/**
 * Update primitive for the {@link Function#_DB_FLUSH} function.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DBFlush extends DBUpdate {
  /** Autoflush option. */
  private final boolean autoflush;

  /**
   * Constructor.
   * @param data data
   * @param info input info
   * @param qc query context
   */
  public DBFlush(final Data data, final InputInfo info, final QueryContext qc) {
    super(UpdateType.DBFLUSH, data, info);
    autoflush = qc.context.options.get(MainOptions.AUTOFLUSH);
  }

  @Override
  public void merge(final Update update) { }

  @Override
  public void apply() {
    if(!autoflush) data.flush(true);
  }

  @Override
  public int size() {
    return 1;
  }

  @Override
  public void prepare() { }
}
