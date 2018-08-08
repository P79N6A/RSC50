package org.basex.util.ft;

import java.util.Locale;

/**
 * Search mode.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public enum FTMode {
  /** All option. */ ALL,
  /** All words option. */ ALL_WORDS,
  /** Any option. */ ANY,
  /** Any word option. */ ANY_WORD,
  /** Phrase search. */ PHRASE;

  @Override
  public String toString() {
    return name().replace('_', ' ').toLowerCase(Locale.ENGLISH);
  }
}
