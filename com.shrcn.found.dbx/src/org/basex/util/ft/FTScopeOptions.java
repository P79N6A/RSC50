package org.basex.util.ft;

import org.basex.util.options.BooleanOption;
import org.basex.util.options.EnumOption;
import org.basex.util.options.Options;

/**
 * Full-text scope options.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class FTScopeOptions extends Options {
  /** Option: same. */
  public static final BooleanOption SAME = new BooleanOption("same", true);
  /** Option: unit. */
  public static final EnumOption<FTBigUnit> UNIT = new EnumOption<>("unit", FTBigUnit.SENTENCE);
}
