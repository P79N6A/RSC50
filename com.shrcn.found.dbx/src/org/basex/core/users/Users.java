package org.basex.core.users;

import static org.basex.core.users.UserText.ADMIN;
import static org.basex.core.users.UserText.INFO;
import static org.basex.core.users.UserText.S_USERINFO;
import static org.basex.core.users.UserText.USER;
import static org.basex.core.users.UserText.USERS;
import static org.basex.util.Token.eq;
import static org.basex.util.Token.string;
import static org.basex.util.Token.token;
import static org.basex.util.XMLAccess.children;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.basex.build.Parser;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.MainOptions;
import org.basex.core.StaticOptions;
import org.basex.core.Text;
import org.basex.io.IO;
import org.basex.io.IOContent;
import org.basex.io.IOFile;
import org.basex.query.value.node.ANode;
import org.basex.query.value.node.DBNode;
import org.basex.query.value.node.FElem;
import org.basex.util.Table;
import org.basex.util.Util;
import org.basex.util.list.StringList;
import org.basex.util.list.TokenList;

/**
 * This class organizes all users.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class Users {
  /** User array. */
  private final LinkedHashMap<String, User> users = new LinkedHashMap<>();
  /** Filename. */
  private final IOFile file;
  /** Info node. */
  private ANode info;

  /**
   * Constructor for global users.
   * @param sopts static options
   */
  public Users(final StaticOptions sopts) {
    file = sopts.dbPath(string(USERS) + IO.XMLSUFFIX);
    read();
    // ensure that default admin user exists
    if(get(ADMIN) == null) add(new User(ADMIN, ADMIN).perm(Perm.ADMIN));
  }

  /**
   * Reads user permissions.
   */
  private synchronized void read() {
    if(!file.exists()) return;
    try {
      final byte[] io = file.read();
      final IOContent content = new IOContent(io, file.path());
      final MainOptions options = new MainOptions(false);
      options.set(MainOptions.INTPARSE, true);
      final ANode doc = new DBNode(Parser.singleParser(content, options, ""));
      final ANode root = children(doc, USERS).next();
      if(root == null) {
        Util.errln(file.name() + ": No '%' root element.", USERS);
      } else {
        for(final ANode child : children(root)) {
          final byte[] qname = child.qname().id();
          if(eq(qname, USER)) {
            try {
              final User user = new User(child);
              final String name = user.name();
              if(users.get(name) != null) {
                Util.errln(file.name() + ": User '%' supplied more than once.", name);
              } else {
                users.put(name, user);
              }
            } catch(final BaseXException ex) {
              // reject users with faulty data
              Util.errln(file.name() + ": " + ex.getLocalizedMessage());
            }
          } else if(eq(qname, INFO)) {
            if(info != null) Util.errln(file.name() + ": occurs more than once: %.", qname);
            else info = child.finish();
          } else {
            Util.errln(file.name() + ": invalid element: %.", qname);
          }
        }
      }
    } catch(final IOException ex) {
      Util.errln(ex);
    }
  }

  /**
   * Writes permissions to disk.
   * @param ctx database context
   */
  public synchronized void write(final Context ctx) {
    if(store()) {
      try {
        file.write(toXML(ctx).serialize().finish());
      } catch(final IOException ex) {
        Util.errln(ex);
      }
    } else if(file.exists()) {
      file.delete();
    }
  }

  /**
   * Writes permissions to the specified XML builder.
   * @param ctx database context
   * @return root element
   */
  private synchronized FElem toXML(final Context ctx) {
    final FElem root = new FElem(USERS);
    for(final User user : users.values()) user.toXML(root);
    return root.add(info().deepCopy(ctx.options));
  }

  /**
   * Adds a user.
   * @param user user to be added
   */
  public synchronized void add(final User user) {
    users.put(user.name(), user);
  }

  /**
   * Renames a user.
   * @param user user reference
   * @param name new name
   */
  public synchronized void alter(final User user, final String name) {
    users.remove(user.name());
    user.name(name);
    users.put(name, user);
  }

  /**
   * Drops a user from the list.
   * @param user user reference
   * @return success flag
   */
  public synchronized boolean drop(final User user) {
    return users.remove(user.name()) != null;
  }

  /**
   * Returns user with the specified name.
   * @param name user name
   * @return user name, or {@code null}
   */
  public synchronized User get(final String name) {
    return users.get(name);
  }

  /**
   * Returns all user names that match the specified pattern.
   * @param pattern glob pattern
   * @return user list
   */
  public synchronized String[] find(final Pattern pattern) {
    final StringList sl = new StringList();
    for(final String name : users.keySet()) {
      if(pattern.matcher(name).matches()) sl.add(name);
    }
    return sl.finish();
  }

  /**
   * Returns table with all users, or users from a specific database.
   * The list will only contain the current user if no admin permissions are available.
   * @param db database (can be {@code null})
   * @param ctx database context
   * @return user information
   */
  public synchronized Table info(final String db, final Context ctx) {
    final Table table = new Table();
    table.description = Text.USERS_X;

    for(final String user : S_USERINFO) table.header.add(user);
    for(final User user : users(db, ctx)) {
      table.contents.add(new TokenList().add(user.name()).add(user.perm(db).toString()));
    }
    return table.sort().toTop(token(ADMIN));
  }

  /**
   * Returns all users, or users that have permissions for a specific database.
   * The list will only contain the current user if no admin permissions are available.
   * @param db database (can be {@code null})
   * @param ctx database context
   * @return user information
   */
  public synchronized ArrayList<User> users(final String db, final Context ctx) {
    final User curr = ctx.user();
    final boolean admin = curr.has(Perm.ADMIN);
    final ArrayList<User> list = new ArrayList<>();
    for(final User user : users.values()) {
      if(admin || curr == user) {
        if(db == null) {
          list.add(user);
        } else {
          final Entry<String, Perm> entry = user.find(db);
          if(entry != null) list.add(user);
        }
      }
    }
    return list;
  }

  /**
   * Returns the info element.
   * @return info element
   */
  public ANode info() {
    return info == null ? new FElem(INFO) : info;
  }

  /**
   * Sets the info element.
   * @param elem info element
   */
  public void info(final ANode elem) {
    info = elem.hasChildren() || elem.attributes().size() != 0 ? elem : null;
  }

  /**
   * Checks if permissions need to be stored.
   * @return result of check
   */
  private synchronized boolean store() {
    if(info != null) return true;
    if(users.size() != 1) return !users.isEmpty();
    final User user = users.values().iterator().next();
    return !user.name().equals(ADMIN) ||
           !user.code(Algorithm.DIGEST, Code.HASH).equals(User.digest(ADMIN, ADMIN));
  }
}
