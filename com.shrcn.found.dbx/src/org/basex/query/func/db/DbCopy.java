package org.basex.query.func.db;

import static org.basex.query.QueryError.BXDB_NAME_X;
import static org.basex.query.QueryError.BXDB_SAME_X;
import static org.basex.query.QueryError.BXDB_WHICH_X;
import static org.basex.util.Token.string;

import org.basex.core.Databases;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.up.primitives.name.DBAlter;
import org.basex.query.up.primitives.name.DBCopy;
import org.basex.query.util.ASTVisitor;
import org.basex.query.value.item.Item;
import org.basex.util.InputInfo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class DbCopy extends DbFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    return copy(qc, true);
  }

  /**
   * Performs the copy function.
   * @param qc query context
   * @param keep keep copied database
   * @return {@code null}
   * @throws QueryException query exception
   */
  final Item copy(final QueryContext qc, final boolean keep) throws QueryException {
    final String name = string(toToken(exprs[0], qc));
    final String newname = string(toToken(exprs[1], qc));

    if(!Databases.validName(name)) throw BXDB_NAME_X.get(info, name);
    if(!Databases.validName(newname)) throw BXDB_NAME_X.get(info, newname);

    // source database does not exist
    if(!qc.context.soptions.dbExists(name)) throw BXDB_WHICH_X.get(info, name);
    if(name.equals(newname)) throw BXDB_SAME_X.get(info, name, newname);

    qc.updates().add(keep ? new DBCopy(name, newname, info, qc) :
      new DBAlter(name, newname, info, qc), qc);
    return null;
  }

  @Override
  public final boolean accept(final ASTVisitor visitor) {
    return dataLock(visitor, 0) || dataLock(visitor, 1) || super.accept(visitor);
  }
}
