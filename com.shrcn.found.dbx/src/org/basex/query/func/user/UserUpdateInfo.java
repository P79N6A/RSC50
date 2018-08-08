package org.basex.query.func.user;

import static org.basex.query.QueryError.ELM_X_X;
import static org.basex.query.QueryError.USER_INFO_X;

import org.basex.core.users.UserText;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.path.NodeTest;
import org.basex.query.up.primitives.Update;
import org.basex.query.up.primitives.UpdateType;
import org.basex.query.up.primitives.UserPermUpdate;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.QNm;
import org.basex.query.value.node.ANode;
import org.basex.util.InputInfo;
import org.basex.util.list.StringList;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class UserUpdateInfo extends UserFn {
  /** Root node test. */
  private static final QNm Q_INFO = new QNm(UserText.INFO);
  /** Root node test. */
  private static final NodeTest T_INFO = new NodeTest(Q_INFO);

  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final ANode node = toElem(exprs[0], qc);
    if(!T_INFO.eq(node)) throw ELM_X_X.get(info, Q_INFO.prefixId(), node);

    qc.updates().add(new UpdateInfo(node.deepCopy(qc.context.options), qc, info), qc);
    return null;
  }

  /** Update primitive. */
  private static final class UpdateInfo extends UserPermUpdate {
    /** Node to be updated. */
    private ANode node;

    /**
     * Constructor.
     * @param node info element
     * @param qc query context
     * @param info input info
     * @throws QueryException query exception
     */
    private UpdateInfo(final ANode node, final QueryContext qc, final InputInfo info)
        throws QueryException {
      super(UpdateType.USERINFO, qc.context.user(), null, new StringList(), qc, info);
      this.node = node;
    }

    @Override
    public void merge(final Update update) throws QueryException {
      throw USER_INFO_X.get(info, operation());
    }

    @Override
    public void apply() {
      users.info(node);
    }

    @Override
    public String operation() { return "updated"; }
  }
}
