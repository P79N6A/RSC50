package org.basex.util.ft;

import org.basex.util.options.EnumOption;
import org.basex.util.options.NumberOption;
import org.basex.util.options.Options;

/**
 * Full-text window options.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FTWindowOptions extends Options {
  /** Option: unit. */
  public static final EnumOption<FTUnit> UNIT = new EnumOption<>("unit", FTUnit.WORDS);
  /** Option: size. */
  public static final NumberOption SIZE = new NumberOption("size", Integer.MAX_VALUE);
}
