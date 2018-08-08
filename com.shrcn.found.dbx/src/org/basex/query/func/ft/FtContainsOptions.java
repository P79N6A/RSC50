package org.basex.query.func.ft;

import org.basex.util.ft.FTCase;
import org.basex.util.ft.FTDiacritics;
import org.basex.util.options.BooleanOption;
import org.basex.util.options.EnumOption;
import org.basex.util.options.StringOption;

/**
 * Full-text options.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FtContainsOptions extends FtIndexOptions {
  /** Option: case. */
  public static final EnumOption<FTCase> CASE = new EnumOption<>("case", FTCase.class);
  /** Option: case. */
  public static final EnumOption<FTDiacritics> DIACRITICS =
      new EnumOption<>("diacritics", FTDiacritics.class);
  /** Option: stemming. */
  public static final BooleanOption STEMMING = new BooleanOption("stemming");
  /** Option: language. */
  public static final StringOption LANGUAGE = new StringOption("language");
}
