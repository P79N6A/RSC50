package org.basex.query.util.collation;

import java.util.HashMap;
import java.util.Locale;

import org.basex.core.BaseXException;
import org.basex.util.Strings;
import org.basex.util.options.Option;
import org.basex.util.options.Options;

/**
 * Collation options.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
abstract class CollationOptions extends Options {
  /**
   * Parses the specified options and returns the faulty key.
   * @param args arguments
   * @return error message
   */
  abstract Collation get(final String args);

  /**
   * Parses the specified options and returns the faulty key.
   * @param args arguments
   * @return error message
   */
  String check(final String args) {
    String error = null;
    for(final String option : Strings.split(args, ';')) {
      final String[] kv = Strings.split(option, '=', 2);
      try {
        assign(kv[0], kv.length == 2 ? kv[1] : "");
      } catch(final BaseXException ex) {
        error = option;
      }
    }
    return error;
  }

  /**
   * Creates an error for an invalid option.
   * @param option option
   * @return error
   */
  protected IllegalArgumentException error(final Option<?> option) {
    return new IllegalArgumentException("Invalid \"" + option + "\" value \"" + get(option) + "\"");
  }

  /** Initialization of locales. */
  protected static class Locales {
    /** Available locales, indexed by language code. */
    static final HashMap<String, Locale> MAP = new HashMap<>();
    static {
      for(final Locale l : Locale.getAvailableLocales()) MAP.put(l.toString().replace('_', '-'), l);
    }
  }
}
