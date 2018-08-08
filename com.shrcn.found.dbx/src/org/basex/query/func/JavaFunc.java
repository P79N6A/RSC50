package org.basex.query.func;

import static org.basex.query.QueryError.JAVAAMB_X_X_X;
import static org.basex.query.QueryError.JAVACONSAMB_X;
import static org.basex.query.QueryError.JAVAERROR_X_X_X;
import static org.basex.query.QueryError.WHICHCONSTR_X_X;
import static org.basex.query.QueryError.WHICHMETHOD_X_X;
import static org.basex.query.QueryText.NAM;
import static org.basex.query.QueryText.PAREN1;
import static org.basex.query.QueryText.PAREN2;
import static org.basex.query.QueryText.SEP;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.basex.core.users.Perm;
import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryModule;
import org.basex.query.StaticContext;
import org.basex.query.expr.Expr;
import org.basex.query.value.Value;
import org.basex.query.value.node.FElem;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.Util;
import org.basex.util.hash.IntObjMap;

/**
 * Java function binding.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class JavaFunc extends JavaFunction {
  /** Java class. */
  private final Class<?> clazz;
  /** Java method. */
  private final String method;
  /** Types provided in the query (can be {@code null}). */
  private final String[] types;

  /**
   * Constructor.
   * @param sc static context
   * @param info input info
   * @param clazz Java class
   * @param method Java method/field
   * @param types types provided in the query (can be {@code null})
   * @param args arguments
   */
  JavaFunc(final StaticContext sc, final InputInfo info, final Class<?> clazz, final String method,
      final String[] types, final Expr[] args) {
    super(sc, info, args, Perm.ADMIN);
    this.clazz = clazz;
    this.method = method;
    this.types = types;
  }

  @Override
  protected Object eval(final Value[] args, final QueryContext qc) throws QueryException {
    try {
      return method.equals(NEW) ? constructor(args) : method(args, qc);
    } catch(final QueryException ex) {
      throw ex;
    } catch(final InvocationTargetException ex) {
      final Throwable cause = ex.getCause();
      throw cause instanceof QueryException ? ((QueryException) cause).info(info) :
        JAVAERROR_X_X_X.get(info, name(), foundArgs(args), cause);
    } catch(final Throwable ex) {
      throw JAVAERROR_X_X_X.get(info, name(), foundArgs(args), ex);
    }
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new JavaFunc(sc, info, clazz, method, types, copyAll(cc, vm, exprs));
  }

  /**
   * Calls a constructor.
   * @param args arguments
   * @return resulting object
   * @throws Exception exception
   */
  private Object constructor(final Value[] args) throws Exception {
    Constructor<?> cons = null;
    Object[] cargs = null;
    for(final Constructor<?> c : clazz.getConstructors()) {
      final Class<?>[] pTypes = c.getParameterTypes();
      if(!typeMatches(pTypes, types)) continue;

      final Object[] jArgs = javaArgs(pTypes, null, args, true);
      if(jArgs != null) {
        if(cons != null) throw JAVACONSAMB_X.get(info, Util.className(clazz) + '#' + pTypes.length);
        cons = c;
        cargs = jArgs;
      }
    }
    if(cons != null) return cons.newInstance(cargs);

    throw WHICHCONSTR_X_X.get(info, name(), foundArgs(args));
  }

  /**
   * Calls a method.
   * @param args arguments
   * @param qc query context
   * @return resulting object
   * @throws Exception exception
   */
  private Object method(final Value[] args, final QueryContext qc) throws Exception {
    // check if a field with the specified name exists
    try {
      final Field field = clazz.getField(method);
      final boolean stat = Modifier.isStatic(field.getModifiers());
      if(args.length == (stat ? 0 : 1)) return field.get(stat ? null : instObj(args[0]));
    } catch(final NoSuchFieldException ex) { /* ignored */ }

    // loop through all methods
    Method meth = null;
    Object inst = null;
    Object[] margs = null;
    for(final Method m : clazz.getMethods()) {
      if(!m.getName().equals(method)) continue;
      final Class<?>[] pTypes = m.getParameterTypes();
      if(!typeMatches(pTypes, types)) continue;

      final boolean stat = Modifier.isStatic(m.getModifiers());
      final Object[] jArgs = javaArgs(pTypes, null, args, stat);
      if(jArgs == null) continue;

      // method found
      if(meth != null) throw JAVAAMB_X_X_X.get(info, clazz.getName(), method, pTypes.length);
      meth = m;
      margs = jArgs;

      if(!stat) {
        inst = instObj(args[0]);
        if(inst instanceof QueryModule) {
          final QueryModule mod = (QueryModule) inst;
          mod.staticContext = sc;
          mod.queryContext = qc;
        }
      }
    }
    if(meth != null) return meth.invoke(inst, margs);

    throw WHICHMETHOD_X_X.get(info, name(), foundArgs(args));
  }

  /**
   * Creates the instance on which a non-static field getter or method is invoked.
   * @param v XQuery value
   * @return Java object
   * @throws QueryException query exception
   */
  private Object instObj(final Value v) throws QueryException {
    return clazz.isInstance(v) ? v : v.toJava();
  }

  /**
   * Returns the function descriptor.
   * @return string
   */
  private String name() {
    return Util.className(clazz) + '.' + method;
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(NAM, clazz.getName() + '.' + method), exprs);
  }

  @Override
  public String description() {
    final StringBuilder sb = new StringBuilder();
    if(method.equals(NEW)) sb.append(NEW).append(' ').append(Util.className(clazz));
    else sb.append(name());
    return sb.append("(...)").toString();
  }

  @Override
  public String toString() {
    return clazz + "." + method + PAREN1 + toString(SEP) + PAREN2;
  }
}
