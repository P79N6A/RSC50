package org.basex.util.ft;

import static org.basex.query.QueryText.CASE;
import static org.basex.query.QueryText.DIACRITICS;
import static org.basex.query.QueryText.FUZZY;
import static org.basex.query.QueryText.LANGUAGE;
import static org.basex.query.QueryText.LOWERCASE;
import static org.basex.query.QueryText.SENSITIVE;
import static org.basex.query.QueryText.STEMMING;
import static org.basex.query.QueryText.THESAURUS;
import static org.basex.query.QueryText.UPPERCASE;
import static org.basex.query.QueryText.USING;
import static org.basex.query.QueryText.WILDCARDS;
import static org.basex.util.Token.TRUE;
import static org.basex.util.ft.FTFlag.DC;
import static org.basex.util.ft.FTFlag.FZ;
import static org.basex.util.ft.FTFlag.ST;
import static org.basex.util.ft.FTFlag.WC;

import java.util.EnumMap;
import java.util.Map.Entry;

import org.basex.data.MetaData;
import org.basex.query.expr.ExprInfo;
import org.basex.query.expr.ft.ThesQuery;
import org.basex.query.value.node.FElem;

/**
 * This class contains all full-text options.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FTOpt extends ExprInfo {
  /** Flag values. */
  private final EnumMap<FTFlag, Boolean> map = new EnumMap<>(FTFlag.class);
  /** Case. */
  public FTCase cs;
  /** Stemming dictionary. */
  public StemDir sd;
  /** Stop words. */
  public StopWords sw;
  /** Thesaurus. */
  public ThesQuery th;
  /** Language. */
  public Language ln;

  /**
   * Adopts the options of the specified argument.
   * @param opt parent full-text options
   * @return self reference
   */
  public FTOpt assign(final FTOpt opt) {
    for(final Entry<FTFlag, Boolean> f : opt.map.entrySet()) {
      final FTFlag fl = f.getKey();
      if(map.get(fl) == null) map.put(fl, f.getValue());
    }
    if(cs == null) cs = opt.cs;
    if(sw == null) sw = opt.sw;
    if(sd == null) sd = opt.sd;
    if(ln == null) ln = opt.ln;
    if(th == null) th = opt.th;
    else if(opt.th != null) th.merge(opt.th);
    return this;
  }

  /**
   * Assigns the full-text options from the specified database meta data.
   * @param md meta data
   * @return self reference
   */
  public FTOpt assign(final MetaData md) {
    set(DC, md.diacritics);
    set(ST, md.stemming);
    cs = md.casesens ? FTCase.SENSITIVE : FTCase.INSENSITIVE;
    ln = md.language;
    return this;
  }

  /**
   * Sets the specified flag.
   * @param flag flag to be set
   * @param value value
   */
  public void set(final FTFlag flag, final boolean value) {
    map.put(flag, value);
  }

  /**
   * Tests if the specified flag has been set.
   * @param flag flag index
   * @return true if flag has been set
   */
  public boolean isSet(final FTFlag flag) {
    return map.containsKey(flag);
  }

  /**
   * Returns the specified flag.
   * @param flag flag index
   * @return flag
   */
  public boolean is(final FTFlag flag) {
    final Boolean b = map.get(flag);
    return b != null && b;
  }

  @Override
  public void plan(final FElem plan) {
    if(is(WC)) plan.add(planAttr(WILDCARDS, TRUE));
    if(is(FZ)) plan.add(planAttr(FUZZY, TRUE));
    if(cs != FTCase.INSENSITIVE) plan.add(planAttr(CASE, cs));
    if(is(DC)) plan.add(planAttr(DIACRITICS, TRUE));
    if(is(ST)) plan.add(planAttr(STEMMING, TRUE));
    if(ln != null) plan.add(planAttr(LANGUAGE, ln));
    if(th != null) plan.add(planAttr(THESAURUS, TRUE));
  }

  @Override
  public String toString() {
    final StringBuilder s = new StringBuilder();
    if(is(WC)) s.append(' ' + USING + ' ' + WILDCARDS);
    if(is(FZ)) s.append(' ' + USING + ' ' + FUZZY);
    if(cs == FTCase.LOWER) s.append(' ' + USING + ' ' + LOWERCASE);
    else if(cs == FTCase.UPPER) s.append(' ' + USING + ' ' + UPPERCASE);
    else if(cs == FTCase.SENSITIVE) s.append(' ' + USING + ' ' + CASE + ' ' + SENSITIVE);
    if(is(DC)) s.append(' ' + USING + ' ' + DIACRITICS + ' ' + SENSITIVE);
    if(is(ST) || sd != null) s.append(' ' + USING + ' ' + STEMMING);
    if(ln != null) s.append(' ' + USING + ' ' + LANGUAGE + " '").append(ln).append('\'');
    if(th != null) s.append(' ' + USING + ' ' + THESAURUS);
    return s.toString();
  }
}
