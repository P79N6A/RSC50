package org.basex.io.serial.dot;

import static org.basex.data.DataText.COMM_C;
import static org.basex.data.DataText.COMM_O;
import static org.basex.data.DataText.PI_C;
import static org.basex.data.DataText.PI_O;
import static org.basex.io.serial.dot.DOTData.COMM;
import static org.basex.io.serial.dot.DOTData.DOTATTR;
import static org.basex.io.serial.dot.DOTData.DOTLINK;
import static org.basex.io.serial.dot.DOTData.DOTNODE;
import static org.basex.io.serial.dot.DOTData.ELEM1;
import static org.basex.io.serial.dot.DOTData.ELEM2;
import static org.basex.io.serial.dot.DOTData.FOOTER;
import static org.basex.io.serial.dot.DOTData.HEADER;
import static org.basex.io.serial.dot.DOTData.ITEM;
import static org.basex.io.serial.dot.DOTData.color;
import static org.basex.util.Token.SPACE;
import static org.basex.util.Token.chop;
import static org.basex.util.Token.concat;
import static org.basex.util.Token.normalize;
import static org.basex.util.Token.string;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.basex.io.out.PrintOutput;
import org.basex.io.serial.OutputSerializer;
import org.basex.io.serial.SerializerMode;
import org.basex.query.QueryException;
import org.basex.query.QueryIOException;
import org.basex.query.util.ft.FTPos;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.QNm;
import org.basex.util.TokenBuilder;
import org.basex.util.Util;
import org.basex.util.list.IntList;

/**
 * This class serializes items in the DOT syntax.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class DOTSerializer extends OutputSerializer {
  /** Compact representation. */
  private final boolean compact;

  /** Cached children. */
  private final ArrayList<IntList> children = new ArrayList<>(1);
  /** Cached attributes. */
  private final TokenBuilder tb = new TokenBuilder();
  /** Cached nodes. */
  private final IntList nodes = new IntList();
  /** Node counter. */
  private int count;

  /**
   * Constructor, defining colors for the dot output.
   * @param os output stream
   * @param compact compact representation
   * @throws IOException I/O exception
   */
  public DOTSerializer(final OutputStream os, final boolean compact) throws IOException {
    super(PrintOutput.get(os), SerializerMode.DEFAULT.get());
    this.compact = compact;
    out.print(HEADER);
  }

  @Override
  public void close() throws IOException {
    indent();
    out.print(FOOTER);
  }

  @Override
  protected void startOpen(final QNm name) {
    tb.reset();
  }

  @Override
  protected void attribute(final byte[] name, final byte[] value, final boolean standalone) {
    tb.addExt(DOTATTR, name, value);
  }

  @Override
  protected void finishOpen() throws IOException {
    final byte[] attr = tb.toArray();
    String color = color(elem.string());
    if(color == null) color = attr.length == 0 ? ELEM1 : ELEM2;
    print(concat(elem.string(), attr), color);
  }

  @Override
  protected void finishEmpty() throws IOException {
    finishOpen();
    finishClose();
  }

  @Override
  protected void finishClose() throws IOException {
    final int c = nodes.get(level);
    final IntList il = child(level);
    final int is = il.size();
    for(int i = 0; i < is; ++i) {
      indent();
      out.print(Util.info(DOTLINK, c, il.get(i)));
    }
    il.reset();
  }

  @Override
  protected void text(final byte[] value, final FTPos ftp) throws IOException {
    print(normalize(value), DOTData.TEXT);
  }

  @Override
  protected void comment(final byte[] value) throws IOException {
    print(new TokenBuilder(COMM_O).add(normalize(value)).add(COMM_C).finish(), COMM);
  }

  @Override
  protected void pi(final byte[] name, final byte[] value) throws IOException {
    print(new TokenBuilder(PI_O).add(name).add(SPACE).add(value).add(PI_C).finish(), DOTData.PI);
  }

  @Override
  protected void atomic(final Item it) throws IOException {
    try {
      print(normalize(it.string(null)), ITEM);
    } catch(final QueryException ex) {
      throw new QueryIOException(ex);
    }
  }

  /**
   * Prints a single node.
   * @param value text
   * @param col color
   * @throws IOException I/O exception
   */
  private void print(final byte[] value, final String col) throws IOException {
    String txt = string(chop(value, 60)).replaceAll("\"|\\r|\\n", "'");
    if(compact) txt = txt.replaceAll("\\\\n\\w+:", "\\\\n");
    indent();
    out.print(Util.info(DOTNODE, count, txt, col));
    nodes.set(level, count);
    if(level > 0) child(level - 1).add(count);
    ++count;
  }

  /**
   * Returns the children from the stack.
   * @param index index
   * @return children
   */
  private IntList child(final int index) {
    while(index >= children.size()) children.add(new IntList());
    return children.get(index);
  }
}
