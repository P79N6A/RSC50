package org.basex.io.parse.json;

import static org.basex.query.QueryError.BXJS_INVALID_X;

import java.util.Stack;

import org.basex.build.json.JsonParserOptions;
import org.basex.build.json.JsonParserOptions.JsonDuplicates;
import org.basex.query.QueryException;
import org.basex.query.QueryIOException;
import org.basex.query.util.list.ValueList;
import org.basex.query.value.Value;
import org.basex.query.value.item.Bln;
import org.basex.query.value.item.Dbl;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.query.value.map.Map;
import org.basex.query.value.seq.Empty;
import org.basex.util.Token;

/**
 * <p>Provides a method for parsing a JSON string and converting it to an XQuery
 * item made of nested maps.
 *
 * <p>The mapping from JSON to XQuery is the following:
 * <p><dl>
 *   <dt>string<dd>xs:string
 *   <dt>number<dd>xs:double
 *   <dt>boolean<dd>xs:boolean
 *   <dt>null<dd>an empty sequence <code>()</code>
 *   <dt>array (e.g. {@code ["foo", true, 123]})
 *   <dd>an XQuery map with integer keys, starting by 1 (e.g.
 *     <code>{1:'foo', 2:true(), 3:123}</code>)
 *   <dt>object (e.g. <code>{"foo": 42, "bar": null}</code>)
 *   <dd>an XQuery map (e.g.
 *     <code>{'foo':42, 'bar':()}</code>)
 * </dl>
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public final class JsonMapConverter extends JsonConverter {
  /** Stack for intermediate values. */
  private final Stack<Value> stack = new Stack<>();
  /** Stack for intermediate array values. */
  private final Stack<ValueList> arrays = new Stack<>();
  /** Stack for intermediate maps values. */
  private final Stack<Map> maps = new Stack<>();

  /**
   * Constructor.
   * @param opts json options
   * @throws QueryIOException query I/O exception
   */
  JsonMapConverter(final JsonParserOptions opts) throws QueryIOException {
    super(opts);
    final JsonDuplicates dupl = jopts.get(JsonParserOptions.DUPLICATES);
    if(dupl == JsonDuplicates.RETAIN) throw new QueryIOException(
        BXJS_INVALID_X.get(null, JsonParserOptions.DUPLICATES.name(), dupl));
  }

  @Override
  public Item finish() {
    final Value v = stack.pop();
    return v.isEmpty() ? null : (Item) v;
  }

  @Override
  void openObject() {
    maps.push(Map.EMPTY);
  }

  @Override
  void openPair(final byte[] key, final boolean add) {
    stack.push(Str.get(key));
  }

  @Override
  void closePair(final boolean add) throws QueryIOException {
    final Value val = stack.pop();
    final Item key = (Item) stack.pop();
    if(add) {
      try {
        maps.push(maps.pop().put(key, val, null));
      } catch(final QueryException ex) {
        throw new QueryIOException(ex);
      }
    }
  }

  @Override
  void closeObject() {
    stack.push(maps.pop());
  }

  @Override
  void openArray() {
    arrays.push(new ValueList());
  }

  @Override
  void openItem() {
  }

  @Override
  void closeItem() {
    arrays.peek().add(stack.pop());
  }

  @Override
  void closeArray() {
    stack.push(arrays.pop().array());
  }

  @Override
  public void numberLit(final byte[] value) throws QueryIOException {
    try {
      stack.push(Dbl.get(value, null));
    } catch(final QueryException ex) {
      throw new QueryIOException(ex);
    }
  }

  @Override
  public void stringLit(final byte[] value) {
    stack.push(Str.get(value));
  }

  @Override
  public void nullLit() {
    stack.push(Empty.SEQ);
  }

  @Override
  public void booleanLit(final byte[] value) {
    stack.push(Bln.get(Token.eq(value, Token.TRUE)));
  }
}
