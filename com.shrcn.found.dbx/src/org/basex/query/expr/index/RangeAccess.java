package org.basex.query.expr.index;

import static org.basex.query.QueryText.DATA;
import static org.basex.query.QueryText.MAX;
import static org.basex.query.QueryText.MIN;
import static org.basex.query.QueryText.TYPE;

import org.basex.data.Data;
import org.basex.index.IndexType;
import org.basex.index.query.IndexIterator;
import org.basex.index.query.NumericRange;
import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.expr.Expr;
import org.basex.query.func.Function;
import org.basex.query.iter.BasicNodeIter;
import org.basex.query.iter.DBNodeIter;
import org.basex.query.util.IndexContext;
import org.basex.query.value.item.Dbl;
import org.basex.query.value.item.Str;
import org.basex.query.value.node.DBNode;
import org.basex.query.value.node.FElem;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.hash.IntObjMap;

/**
 * This index class retrieves range values from the index.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class RangeAccess extends IndexAccess {
  /** Index type. */
  private final NumericRange index;

  /**
   * Constructor.
   * @param info input info
   * @param index index reference
   * @param ictx index context
   */
  public RangeAccess(final InputInfo info, final NumericRange index, final IndexContext ictx) {
    super(ictx, info);
    this.index = index;
  }

  @Override
  public BasicNodeIter iter(final QueryContext qc) {
    final byte kind = index.type() == IndexType.TEXT ? Data.TEXT : Data.ATTR;
    return new DBNodeIter(ictx.data) {
      final IndexIterator it = data.iter(index);
      @Override
      public DBNode next() {
        return it.more() ? new DBNode(data, it.pre(), kind) : null;
      }
    };
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new RangeAccess(info, index, ictx);
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(DATA, ictx.data.meta.name, MIN, index.min, MAX, index.max,
        TYPE, index.type()));
  }

  @Override
  public String toString() {
    final Function func = index.type() == IndexType.TEXT ? Function._DB_TEXT_RANGE :
      Function._DB_ATTRIBUTE_RANGE;
    return func.toString(Str.get(ictx.data.meta.name), Dbl.get(index.min), Dbl.get(index.max));
  }
}
