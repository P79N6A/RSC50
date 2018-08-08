package org.basex.query.value.item;

import static org.basex.query.QueryError.FUNZONE_X_X;
import static org.basex.query.QueryError.YEARRANGE_X;
import static org.basex.query.QueryText.XDTM;

import java.math.BigDecimal;
import java.util.Date;

import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.value.type.AtomType;
import org.basex.util.DateTime;
import org.basex.util.InputInfo;
import org.basex.util.Token;
import org.basex.util.Util;

/**
 * DateTime item ({@code xs:dateTime}).
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Dtm extends ADate {
  /**
   * Constructor.
   * @param date date
   */
  public Dtm(final ADate date) {
    super(AtomType.DTM, date);
    if(hou == -1) {
      hou = 0;
      min = 0;
      sec = BigDecimal.ZERO;
    }
  }

  /**
   * Constructor.
   * @param date date
   * @param time time
   * @param ii input info
   * @throws QueryException query exception
   */
  public Dtm(final Dat date, final Tim time, final InputInfo ii) throws QueryException {
    super(AtomType.DTM, date);

    hou = time.hou;
    min = time.min;
    sec = time.sec;
    if(tz == Short.MAX_VALUE) {
      tz = time.tz;
    } else if(tz != time.tz && time.tz != Short.MAX_VALUE) {
      throw FUNZONE_X_X.get(ii, date, time);
    }
  }

  /**
   * Constructor.
   * @param date date
   * @param ii input info
   * @throws QueryException query exception
   */
  public Dtm(final byte[] date, final InputInfo ii) throws QueryException {
    super(AtomType.DTM);
    final int i = Token.indexOf(date, 'T');
    if(i == -1) throw dateError(date, XDTM, ii);
    date(Token.substring(date, 0, i), XDTM, ii);
    time(Token.substring(date, i + 1), XDTM, ii);
  }

  /**
   * Constructor.
   * @param date date
   * @param dur duration
   * @param plus plus/minus flag
   * @param ii input info
   * @throws QueryException query exception
   */
  public Dtm(final Dtm date, final Dur dur, final boolean plus, final InputInfo ii)
      throws QueryException {

    this(date);
    if(dur instanceof DTDur) {
      calc((DTDur) dur, plus);
      if(yea <= MIN_YEAR || yea > MAX_YEAR) throw YEARRANGE_X.get(ii, yea);
    } else {
      calc((YMDur) dur, plus, ii);
    }
  }

  @Override
  public void timeZone(final DTDur zone, final boolean spec, final InputInfo ii)
      throws QueryException {
    tz(zone, spec, ii);
  }

  @Override
  public boolean sameAs(final Expr cmp) {
    if(!(cmp instanceof Dtm)) return false;
    final Dtm dtm = (Dtm) cmp;
    return type == dtm.type && yea == dtm.yea && mon == dtm.mon && day == dtm.day &&
        hou == dtm.hou && min == dtm.min && tz == dtm.tz &&
        sec == null ? dtm.sec == null : sec.compareTo(dtm.sec) == 0;
  }

  /**
   * Returns a dateTime item for the specified milliseconds.
   * @param ms milliseconds since January 1, 1970, 00:00:00 GMT
   * @return dateTime instance
   */
  public static Dtm get(final long ms) {
    try {
      return new Dtm(Token.token(DateTime.format(new Date(ms))), null);
    } catch(final QueryException ex) {
      throw Util.notExpected();
    }
  }
}
