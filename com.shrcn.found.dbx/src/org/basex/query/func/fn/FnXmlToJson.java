package org.basex.query.func.fn;

import static org.basex.query.QueryError.INVALIDOPT_X;

import org.basex.build.json.JsonOptions;
import org.basex.build.json.JsonOptions.JsonFormat;
import org.basex.build.json.JsonSerialOptions;
import org.basex.io.serial.SerialMethod;
import org.basex.io.serial.SerializerOptions;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.query.value.node.ANode;
import org.basex.util.InputInfo;
import org.basex.util.options.Options.YesNo;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class FnXmlToJson extends FnParseJson {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final ANode node = toEmptyNode(exprs[0], qc);
    final JsonSerialOptions opts = toOptions(1, new JsonSerialOptions(), qc);
    if(node == null) return null;

    final JsonSerialOptions jopts = new JsonSerialOptions();
    jopts.set(JsonOptions.FORMAT, JsonFormat.BASIC);

    final SerializerOptions sopts = new SerializerOptions();
    sopts.set(SerializerOptions.METHOD, SerialMethod.JSON);
    sopts.set(SerializerOptions.JSON, jopts);
    sopts.set(SerializerOptions.INDENT, opts.get(JsonSerialOptions.INDENT) ? YesNo.YES : YesNo.NO);
    return Str.get(serialize(node.iter(), sopts, INVALIDOPT_X));
  }
}
