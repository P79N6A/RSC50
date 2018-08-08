package org.basex.query.func.client;

import static org.basex.query.QueryError.BXCL_COMM_X;
import static org.basex.query.QueryError.BXCL_FITEM_X;
import static org.basex.query.QueryError.BXCL_QUERY_X;
import static org.basex.query.QueryError.get;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.basex.api.client.ClientSession;
import org.basex.core.BaseXException;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryIOException;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.item.QNm;
import org.basex.query.value.type.FuncType;
import org.basex.query.value.type.Type;
import org.basex.util.Token;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class ClientQuery extends ClientFn {
  /** Query pattern. */
  private static final Pattern QUERYPAT = Pattern.compile("\\[(.*?)\\] (.*)", Pattern.MULTILINE);

  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    checkCreate(qc);
    final ClientSession cs = session(qc, false);
    final String query = Token.string(toToken(exprs[1], qc));
    final ValueBuilder vb = new ValueBuilder();
    try(org.basex.api.client.ClientQuery cq = cs.query(query)) {
      // bind variables and context value
      for(final Entry<String, Value> binding : toBindings(2, qc).entrySet()) {
        final String key = binding.getKey();
        final Value value = binding.getValue();
        if(key.isEmpty()) cq.context(value);
        else cq.bind(key, value);
      }
      // evaluate query
      cq.cache(true);
      while(cq.more()) {
        final String result = cq.next();
        final Type tp = cq.type();
        if(tp instanceof FuncType) throw BXCL_FITEM_X.get(info, result);
        vb.add(cq.type().castString(result, qc, sc, info));
      }
      return vb.value();
    } catch(final QueryIOException ex) {
      throw ex.getCause(info);
    } catch(final BaseXException ex) {
      final Matcher m = QUERYPAT.matcher(ex.getMessage());
      if(m.find()) {
        final String name = m.group(1), msg = m.group(2);
        final QueryException exc = get(name, msg, info);
        throw exc == null ? new QueryException(info, new QNm(name), msg) : exc;
      }
      throw BXCL_QUERY_X.get(info, ex);
    } catch(final IOException ex) {
      throw BXCL_COMM_X.get(info, ex);
    }
  }
}
