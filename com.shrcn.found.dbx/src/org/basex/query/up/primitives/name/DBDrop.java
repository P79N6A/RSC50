package org.basex.query.up.primitives.name;

import static org.basex.query.QueryError.UPDBERROR_X_X;

import org.basex.core.cmd.DropDB;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.Function;
import org.basex.query.up.primitives.UpdateType;
import org.basex.util.InputInfo;

/**
 * Update primitive for the {@link Function#_DB_DROP} function.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Lukas Kircher
 */
public final class DBDrop extends NameUpdate {
  /**
   * Constructor.
   * @param name name of database
   * @param info input info
   * @param qc query context
   */
  public DBDrop(final String name, final InputInfo info, final QueryContext qc) {
    super(UpdateType.DBDROP, name, info, qc);
  }

  @Override
  public void prepare() { }

  @Override
  public void apply() throws QueryException {
    close();
    // check if database files can be safely removed
    if(!DropDB.drop(name, qc.context.soptions)) throw UPDBERROR_X_X.get(info, name, operation());
  }

  @Override
  protected String operation() { return "dropped"; }
}
