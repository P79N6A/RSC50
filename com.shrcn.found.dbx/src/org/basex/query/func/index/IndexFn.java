package org.basex.query.func.index;

import static org.basex.query.QueryError.BXDB_INDEX_X;
import static org.basex.util.Token.token;

import org.basex.data.Data;
import org.basex.index.Index;
import org.basex.index.IndexType;
import org.basex.index.query.EntryIterator;
import org.basex.index.query.IndexEntries;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.util.ASTVisitor;
import org.basex.query.value.node.FElem;

/**
 * Index function.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public abstract class IndexFn extends StandardFunc {
  /** Name: count. */
  static final String COUNT = "count";
  /** Name: value. */
  static final String ENTRY = "entry";

  @Override
  public final boolean accept(final ASTVisitor visitor) {
    return dataLock(visitor, 0) && super.accept(visitor);
  }

  /**
   * Returns all entries of the specified value index.
   * @param data data reference
   * @param entries container for returning index entries
   * @param call calling function
   * @return text entries
   * @throws QueryException query exception
   */
  public static Iter entries(final Data data, final IndexEntries entries, final StandardFunc call)
      throws QueryException {

    final IndexType type = entries.type();
    if(!data.meta.index(type)) throw BXDB_INDEX_X.get(call.info, data.meta.name, type);
    return entries(data.index(type), entries);
  }

  /**
   * Returns all entries of the specified index.
   * @param index index
   * @param entries entries token
   * @return entry iterator
   */
  static Iter entries(final Index index, final IndexEntries entries) {
    return new Iter() {
      final EntryIterator ei = index.entries(entries);
      @Override
      public FElem next() {
        final byte[] token = ei.next();
        return token == null ? null : new FElem(ENTRY).add(COUNT, token(ei.count())).add(token);
      }
    };
  }
}
