package org.basex.core.cmd;

import static org.basex.core.Text.COLS;
import static org.basex.core.Text.NL;

import org.basex.core.Command;
import org.basex.core.users.Perm;
import org.basex.data.MetaData;
import org.basex.data.MetaProp;
import org.basex.util.TokenBuilder;

/**
 * Abstract class for database info.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
abstract class AInfo extends Command {
  /**
   * Protected constructor.
   * @param openDB requires opened database
   * @param args arguments
   */
  AInfo(final boolean openDB, final String... args) {
    super(Perm.READ, openDB, args);
  }

  /**
   * Formats the specified input.
   * @param tb token builder
   * @param key key
   * @param val value
   */
  static void info(final TokenBuilder tb, final Object key, final Object val) {
    tb.add(' ').add(key.toString()).add(COLS).add(val.toString()).add(NL);
  }

  /**
   * Formats the specified input.
   * @param tb token builder
   * @param prop meta property
   * @param meta meta data
   */
  static void info(final TokenBuilder tb, final MetaProp prop, final MetaData meta) {
    info(tb, prop.name(), prop.value(meta));
  }
}
