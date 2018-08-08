package org.basex.query;

import org.basex.query.func.StaticFunc;
import org.basex.query.util.ASTVisitor;
import org.basex.query.value.item.QNm;
import org.basex.query.value.node.FElem;
import org.basex.query.var.StaticVar;
import org.basex.util.hash.TokenObjMap;
import org.basex.util.hash.TokenSet;

/**
 * An XQuery library module.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Leo Woerteler
 */
public final class LibraryModule extends Module {
  /** Library name. */
  public final QNm name;

  /**
   * Constructor.
   * @param name of library
   * @param doc documentation
   * @param funcs user-defined functions
   * @param vars static variables
   * @param modules imported modules
   * @param sc static context
   */
  LibraryModule(final QNm name, final String doc, final TokenObjMap<StaticFunc> funcs,
      final TokenObjMap<StaticVar> vars, final TokenSet modules, final StaticContext sc) {
    super(sc, null, doc, null, funcs, vars, modules);
    this.name = name;
  }

  /**
   * Returns the module namespace URI.
   * @return URI
   */
  public byte[] uri() {
    return name.uri();
  }

  @Override
  public boolean visit(final ASTVisitor visitor) {
    return true;
  }

  @Override
  public void comp(final CompileContext cc) {
  }

  @Override
  public String toString() {
    return "";
  }

  @Override
  public void plan(final FElem e) {
  }
}
