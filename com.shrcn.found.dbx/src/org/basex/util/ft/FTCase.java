package org.basex.util.ft;

import java.util.Locale;

/**
 * Full-text cases.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public enum FTCase {
  /** Sensitive.   */ SENSITIVE,
  /** Insensitive. */ INSENSITIVE,
  /** Lower-case.  */ LOWER,
  /** Upper-case.  */ UPPER;

  /**
   * Returns a string representation.
   * @return string representation
   */
  @Override
  public String toString() {
    return name().toLowerCase(Locale.ENGLISH);
  }
}
