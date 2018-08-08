package org.basex.query.func.jobs;

import org.basex.util.options.BooleanOption;
import org.basex.util.options.Options;
import org.basex.util.options.StringOption;

/** Scheduling options. */
public final class EvalOptions extends Options {
  /** Query base-uri. */
  public static final StringOption BASE_URI = new StringOption("base-uri");
  /** Cache result. */
  public static final BooleanOption CACHE = new BooleanOption("cache", false);
  /** Start date/time/duration. */
  public static final StringOption START = new StringOption("start", "");
  /** End date/duration. */
  public static final StringOption END = new StringOption("end", "");
  /** Interval after which query will be repeated. */
  public static final StringOption INTERVAL = new StringOption("interval", "");
  /** Custom id string. */
  public static final StringOption ID = new StringOption("id");
}
