package org.basex.util.ft;

import java.util.Locale;

/**
 * Full-text content types.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public enum FTContents {
  /** At start.       */ START,
  /** At end.         */ END,
  /** Entire content. */ ENTIRE;

  /**
   * Returns a string representation.
   * @return string representation
   */
  @Override
  public String toString() {
    return name().toLowerCase(Locale.ENGLISH);
  }
}
