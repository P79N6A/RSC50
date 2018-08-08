package org.basex.query.up.primitives.name;

import static org.basex.query.QueryError.UPDBOPTERR_X;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.basex.build.Parser;
import org.basex.core.MainOptions;
import org.basex.core.cmd.Close;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.Optimize;
import org.basex.data.Data;
import org.basex.data.DataClip;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.Function;
import org.basex.query.up.primitives.DBNew;
import org.basex.query.up.primitives.DBOptions;
import org.basex.query.up.primitives.NewInput;
import org.basex.query.up.primitives.UpdateType;
import org.basex.util.InputInfo;
import org.basex.util.Util;
import org.basex.util.options.Option;
import org.basex.util.options.Options;

/**
 * Update primitive for the {@link Function#_DB_CREATE} function.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Lukas Kircher
 */
public final class DBCreate extends NameUpdate {
  /** Container for new database documents. */
  private final DBNew newDocs;
  /** Database update options. */
  private final DBOptions options;

  /**
   * Constructor.
   * @param name name for created database
   * @param input input (ANode and QueryInput references)
   * @param opts database options
   * @param qc query context
   * @param info input info
   * @throws QueryException query exception
   */
  public DBCreate(final String name, final List<NewInput> input, final Options opts,
      final QueryContext qc, final InputInfo info) throws QueryException {

    super(UpdateType.DBCREATE, name, info, qc);
    final ArrayList<Option<?>> supported = new ArrayList<>();
    Collections.addAll(supported, DBOptions.INDEXING);
    Collections.addAll(supported, DBOptions.PARSING);
    options = new DBOptions(opts, supported, info);
    newDocs = new DBNew(qc, input, options, info);
  }

  @Override
  public void prepare() throws QueryException {
    newDocs.prepare(name);
  }

  @Override
  public void apply() throws QueryException {
    close();

    final MainOptions mopts = options.assignTo(new MainOptions(qc.context.options, true));
    final boolean add = newDocs.data != null;
    try {
      final Data data = CreateDB.create(name, Parser.emptyParser(mopts), qc.context, mopts);

      // add initial documents and optimize database
      if(add) {
        data.startUpdate(mopts);
        try {
          data.insert(data.meta.size, -1, new DataClip(newDocs.data));
          Optimize.optimize(data, null);
        } finally {
          data.finishUpdate(mopts);
        }
      }
      Close.close(data, qc.context);

    } catch(final IOException ex) {
      throw UPDBOPTERR_X.get(info, ex);
    } finally {
      if(add) newDocs.finish();
    }
  }

  @Override
  public String toString() {
    return Util.className(this) + '[' + newDocs.inputs + ']';
  }

  @Override
  public String operation() { return "created"; }
}
