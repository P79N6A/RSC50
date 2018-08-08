package org.basex.core.cmd;

import static org.basex.core.Text.COL;
import static org.basex.core.Text.GENERAL_INFO;
import static org.basex.core.Text.GLOBAL_OPTIONS;
import static org.basex.core.Text.LOCAL_OPTIONS;
import static org.basex.core.Text.NL;
import static org.basex.core.Text.USED_MEM;
import static org.basex.core.Text.VERSINFO;

import java.io.IOException;

import org.basex.core.Context;
import org.basex.core.MainOptions;
import org.basex.core.StaticOptions;
import org.basex.core.locks.LockResult;
import org.basex.core.users.Perm;
import org.basex.core.users.User;
import org.basex.util.Performance;
import org.basex.util.Prop;
import org.basex.util.TokenBuilder;
import org.basex.util.options.Option;

/**
 * Evaluates the 'info' command and returns general database information.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Info extends AInfo {
  /**
   * Default constructor.
   */
  public Info() {
    super(false);
  }

  @Override
  protected boolean run() throws IOException {
    out.print(info(context));
    return true;
  }

  @Override
  public void databases(final LockResult lr) {
    // no locks needed
  }

  /**
   * Creates a database information string.
   * @param context database context
   * @return info string
   */
  public static String info(final Context context) {
    final TokenBuilder tb = new TokenBuilder();
    tb.add(GENERAL_INFO + COL + NL);
    info(tb, VERSINFO, Prop.VERSION);

    final User user = context.user();
    info(tb, USED_MEM, Performance.getMemory());

    if(user.has(Perm.ADMIN)) {
      final StaticOptions sopts = context.soptions;
      tb.add(NL + GLOBAL_OPTIONS + COL + NL);
      for(final Option<?> o : sopts) info(tb, o.name(), sopts.get(o));
    }

    final MainOptions opts = context.options;
    tb.add(NL + LOCAL_OPTIONS + NL);
    for(final Option<?> o : opts) info(tb, o.name(), opts.get(o));
    return tb.toString();
  }
}
