package org.basex.query.up.primitives.db;

import static org.basex.query.QueryError.UPPUTERR_X;

import java.io.IOException;

import org.basex.data.Data;
import org.basex.io.out.PrintOutput;
import org.basex.io.serial.Serializer;
import org.basex.io.serial.SerializerMode;
import org.basex.io.serial.SerializerOptions;
import org.basex.query.QueryException;
import org.basex.query.up.primitives.Update;
import org.basex.query.up.primitives.UpdateType;
import org.basex.query.value.node.DBNode;
import org.basex.util.InputInfo;
import org.basex.util.Util;
import org.basex.util.list.StringList;

/**
 * Fn:put operation primitive.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Lukas Kircher
 */
public final class Put extends DBUpdate {
  /** Target paths. The same node can be stored in multiple locations. */
  private final StringList paths = new StringList(1);
  /** Node id of the target node. Target nodes are identified via their ID, as structural
   *  changes (delete/insert) during the snapshot lead to PRE value shifts on disk.
   *  In addition, deleted/replaced nodes will not be serialized by fn:put as the
   *  identity of these nodes is gone - which is easier to track operating on IDs. */
  public final int id;

  /**
   * Constructor.
   * @param id target node id
   * @param data target data reference
   * @param path target path
   * @param info input info
   */
  public Put(final int id, final Data data, final String path, final InputInfo info) {
    super(UpdateType.FNPUT, data, info);
    this.id = id;
    paths.add(path);
  }

  @Override
  public void apply() throws QueryException {
    for(final String u : paths) {
      final int pre = data.pre(id);
      if(pre == -1) return;
      final DBNode node = new DBNode(data, pre);
      try(final PrintOutput po = new PrintOutput(u)) {
        final SerializerOptions sopts = SerializerMode.DEFAULT.get();
        try(final Serializer ser = Serializer.get(po, sopts)) {
          ser.serialize(node);
        }
      } catch(final IOException ex) {
        throw UPPUTERR_X.get(info, u);
      }
    }
  }

  @Override
  public void merge(final Update update) {
    for(final String path : ((Put) update).paths) paths.add(path);
  }

  @Override
  public int size() {
    return paths.size();
  }

  @Override
  public String toString() {
    return Util.className(this) + '[' + id + ", " + paths.get(0) + ']';
  }

  @Override
  public void prepare() { }
}
