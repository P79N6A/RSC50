package org.basex.core.users;

import static org.basex.core.users.UserText.ALGORITHM;
import static org.basex.core.users.UserText.DATABASE;
import static org.basex.core.users.UserText.NAME;
import static org.basex.core.users.UserText.PASSWORD;
import static org.basex.core.users.UserText.PATTERN;
import static org.basex.core.users.UserText.PERMISSION;
import static org.basex.core.users.UserText.USER;
import static org.basex.util.Strings.md5;
import static org.basex.util.Strings.sha256;
import static org.basex.util.Token.eq;
import static org.basex.util.Token.string;
import static org.basex.util.XMLAccess.attribute;
import static org.basex.util.XMLAccess.children;
import static org.basex.util.XMLAccess.value;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.basex.core.BaseXException;
import org.basex.core.Databases;
import org.basex.query.value.node.ANode;
import org.basex.query.value.node.FElem;
import org.basex.util.Prop;

/**
 * This class contains information on a single user.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class User {
  /** Stored password codes. */
  private final EnumMap<Algorithm, EnumMap<Code, String>> passwords =
      new EnumMap<>(Algorithm.class);
  /** Local permissions, using database names or glob patterns as key. */
  private final LinkedHashMap<String, Perm> locals = new LinkedHashMap<>();
  /** User name. */
  private String name;
  /** Permission. */
  private Perm perm;

  /**
   * Constructor.
   * @param name user name
   * @param password password
   */
  public User(final String name, final String password) {
    this.name = name;
    this.perm = Perm.NONE;
    for(final Algorithm algo : Algorithm.values()) {
      passwords.put(algo, new EnumMap<Code, String>(Code.class));
    }
    password(password);
  }

  /**
   * Parses a single user from the specified node.
   * @param user user node
   * @throws BaseXException database exception
   */
  User(final ANode user) throws BaseXException {
    name = string(attribute("Root", user, NAME));
    perm = attribute(name, user, PERMISSION, Perm.values());

    for(final ANode child : children(user)) {
      if(eq(child.qname().id(), PASSWORD)) {
        final EnumMap<Code, String> ec = new EnumMap<>(Code.class);
        final Algorithm algo = attribute(name, child, ALGORITHM, Algorithm.values());
        if(passwords.containsKey(algo)) throw new BaseXException(
            name + ": Algorithm \"" + algo + "\" supplied more than once.");
        passwords.put(algo, ec);

        for(final ANode code : children(child)) {
          final Code cd = value(name, code.qname().id(), algo.codes);
          if(ec.containsKey(cd)) throw new BaseXException(
              name + ", " + algo + ": Code \"" + code + "\" supplied more than once.");
          ec.put(cd, string(code.string()));
        }
        for(final Code code : algo.codes) {
          if(ec.get(code) == null)
            throw new BaseXException(name + ", " + algo + ": Code \"" + code + "\" missing.");
        }
      } else if(eq(child.qname().id(), DATABASE)) {
        // parse local permissions
        final String nm = string(attribute(name, child, PATTERN));
        final Perm prm = attribute(name, child, PERMISSION, Perm.values());
        locals.put(nm, prm);
      } else {
        throw new BaseXException(name + ": invalid element: <" + child.qname() + "/>.");
      }
    }

    // create missing entries
    for(final Algorithm algo : Algorithm.values()) {
      if(passwords.get(algo) == null) throw new BaseXException(
          name + ": Algorithm \"" + algo + "\" missing.");
    }
  }

  /**
   * Writes permissions to the specified XML builder.
   * @param root root element
   */
  synchronized void toXML(final FElem root) {
    final FElem user = new FElem(USER).add(NAME, name).add(PERMISSION, perm.toString());
    for(final Entry<Algorithm, EnumMap<Code, String>> algo : passwords.entrySet()) {
      final FElem pw = new FElem(PASSWORD).add(ALGORITHM, algo.getKey().toString());
      for(final Entry<Code, String> code : algo.getValue().entrySet()) {
        final String v = code.getValue();
        if(!v.isEmpty()) pw.add(new FElem(code.getKey().toString()).add(v));
      }
      user.add(pw);
    }
    for(final Entry<String, Perm> local : locals.entrySet()) {
      user.add(new FElem(DATABASE).add(PATTERN, local.getKey()).
          add(PERMISSION, local.getValue().toString()));
    }
    root.add(user);
  }

  /**
   * Sets the user name.
   * @param nm name
   */
  void name(final String nm) {
    name = nm;
  }

  /**
   * Drops the specified database pattern.
   * @param pattern database pattern
   */
  public synchronized void drop(final String pattern) {
    locals.remove(pattern);
  }

  /**
   * Returns the local permissions.
   * @return local permissions
   */
  public Map<String, Perm> locals() {
    return locals;
  }

  /**
   * Returns the user name.
   * @return name
   */
  public String name() {
    return name;
  }

  /**
   * Computes new password hashes.
   * @param password password (plain text)
   */
  public synchronized void password(final String password) {
    EnumMap<Code, String> codes = passwords.get(Algorithm.DIGEST);
    codes.put(Code.HASH, digest(name, password));

    codes = passwords.get(Algorithm.SALTED_SHA256);
    final String salt = Long.toString(System.nanoTime());
    codes.put(Code.SALT, salt);
    codes.put(Code.HASH, sha256(salt + password));
  }

  /**
   * Returns the specified code.
   * @param alg used algorithm
   * @param code code to be returned
   * @return code, or {@code null} if code does not exist
   */
  public String code(final Algorithm alg, final Code code) {
    return passwords.get(alg).get(code);
  }

  /**
   * Returns algorithms.
   * @return algorithms
   */
  public EnumMap<Algorithm, EnumMap<Code, String>> alg() {
    return passwords;
  }

  /**
   * Returns the global permission, or the permission for the specified database.
   * @param db database (can be {@code null})
   * @return permission
   */
  public Perm perm(final String db) {
    if(db != null) {
      final Entry<String, Perm> entry = find(db);
      if(entry != null) return entry.getValue();
    }
    return perm;
  }

  /**
   * Returns the first entry for the specified database.
   * @param db database
   * @return entry, or {@code null}
   */
  synchronized Entry<String, Perm> find(final String db) {
    for(final Entry<String, Perm> entry : locals.entrySet()) {
      if(Databases.regex(entry.getKey()).matcher(db).matches()) return entry;
    }
    return null;
  }

  /**
   * Sets the global permission.
   * @param prm permission
   * @return self reference
   */
  public synchronized User perm(final Perm prm) {
    perm = prm;
    return this;
  }

  /**
   * Sets the permission.
   * @param prm permission
   * @param pattern database pattern (can be empty)
   * @return self reference
   */
  public synchronized User perm(final Perm prm, final String pattern) {
    if(pattern.isEmpty()) {
      perm(prm);
    } else {
      locals.put(pattern, prm);
    }
    return this;
  }


  /**
   * Tests if the user has the specified permission.
   * @param prm permission to be checked
   * @return result of check
   */
  public boolean has(final Perm prm) {
    return has(prm, null);
  }

  /**
   * Tests if the user has the specified permission.
   * @param prm permission to be checked
   * @param db database (can be {@code null})
   * @return result of check
   */
  public boolean has(final Perm prm, final String db) {
    return perm(db).ordinal() >= prm.ordinal();
  }

  /**
   * Computes the hash from the specified password and checks if it is correct.
   * @param password (plain text)
   * @return name
   */
  public boolean matches(final String password) {
    final EnumMap<Code, String> alg = passwords.get(Algorithm.SALTED_SHA256);
    return sha256(alg.get(Code.SALT) + password).equals(alg.get(Code.HASH));
  }

  /**
   * Returns the digest hash value.
   * @param name user name
   * @param password password
   * @return digest digest hash
   */
  static String digest(final String name, final String password) {
    return md5(name + ':' + Prop.NAME + ':' + password);
  }
}
