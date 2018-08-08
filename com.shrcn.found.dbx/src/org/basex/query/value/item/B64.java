package org.basex.query.value.item;

import static org.basex.query.QueryError.castError;

import org.basex.query.QueryException;
import org.basex.query.StaticContext;
import org.basex.query.util.collation.Collation;
import org.basex.query.value.type.AtomType;
import org.basex.util.InputInfo;
import org.basex.util.Token;
import org.basex.util.Util;

/**
 * Base64 item ({@code xs:base64Binary}).
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public class B64 extends Bin {
  /**
   * Empty constructor.
   */
  B64() {
    super(null, AtomType.B64);
  }

  /**
   * Constructor.
   * @param data binary data
   */
  public B64(final byte[] data) {
    super(data, AtomType.B64);
  }

  /**
   * Constructor.
   * @param value textual representation
   * @param ii input info
   * @throws QueryException query exception
   */
  public B64(final byte[] value, final InputInfo ii) throws QueryException {
    super(decode(value, ii), AtomType.B64);
  }

  /**
   * Constructor.
   * @param bin base64 input
   * @param ii input info
   * @throws QueryException query exception
   */
  public B64(final Bin bin, final InputInfo ii) throws QueryException {
    this(bin.binary(ii));
  }

  @Override
  public byte[] string(final InputInfo ii) throws QueryException {
    return org.basex.util.Base64.encode(binary(ii));
  }

  @Override
  public final boolean eq(final Item it, final Collation coll, final StaticContext sc,
      final InputInfo ii) throws QueryException {
    return Token.eq(binary(ii), it instanceof Bin ? ((Bin) it).binary(ii) :
      decode(it.string(ii), ii));
  }

  @Override
  public final int diff(final Item it, final Collation coll, final InputInfo ii)
      throws QueryException {
    return Token.diff(binary(ii), it instanceof Bin ? ((Bin) it).binary(ii) :
      decode(it.string(ii), ii));
  }

  /**
   * Converts the input into a byte array.
   * @param d textual data
   * @param ii input info
   * @return decoded string
   * @throws QueryException query exception
   */
  private static byte[] decode(final byte[] d, final InputInfo ii) throws QueryException {
    try {
      return org.basex.util.Base64.decode(d);
    } catch(final IllegalArgumentException ex) {
      throw castError(AtomType.B64, ex.getMessage().replaceAll("^.*?: |\\.$", ""), ii);
    }
  }

  @Override
  public String toString() {
    return Util.info("\"%\"", org.basex.util.Base64.encode(data));
  }
}
