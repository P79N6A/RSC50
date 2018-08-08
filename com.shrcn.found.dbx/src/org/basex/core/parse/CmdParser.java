package org.basex.core.parse;

import java.util.ArrayList;

import org.basex.core.Command;
import org.basex.core.Context;
import org.basex.query.QueryException;

/**
 * This is an interface for parsing database commands.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
abstract class CmdParser {
  /** Input string. */
  final String input;
  /** Context. */
  final Context ctx;

  /** Suggest possible completions. */
  boolean suggest;
  /** Parse input as single command. */
  boolean single;
  /** Password reader. */
  PasswordReader pwReader;

  /**
   * Constructor.
   * @param input input string
   * @param ctx database context
   */
  CmdParser(final String input, final Context ctx) {
    this.ctx = ctx;
    this.input = input;
  }

  /**
   * Attaches a password reader.
   * @param pr password reader
   */
  final void pwReader(final PasswordReader pr) {
    pwReader = pr;
  }

  /**
   * Parses the input and returns a command list.
   * @param cmds container for created commands
   * @param sngl parse input as single command
   * @param sggst suggest flag
   * @throws QueryException query exception
   */
  final void parse(final ArrayList<Command> cmds, final boolean sngl, final boolean sggst)
      throws QueryException {

    single = sngl;
    suggest = sggst;
    parse(cmds);
  }

  /**
   * Parses the input and fills the command list.
   * @param cmds container for created commands
   * @throws QueryException query exception
   */
  protected abstract void parse(final ArrayList<Command> cmds) throws QueryException;
}
