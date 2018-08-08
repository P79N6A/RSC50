package org.basex.query.func;

import static org.basex.query.QueryError.FUNCPRIVATE_X;
import static org.basex.query.QueryText.FUNC;
import static org.basex.query.QueryText.NAM;
import static org.basex.query.QueryText.SEP;
import static org.basex.query.QueryText.TCL;

import org.basex.query.CompileContext;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.StaticContext;
import org.basex.query.ann.Annotation;
import org.basex.query.expr.Arr;
import org.basex.query.expr.Expr;
import org.basex.query.util.ASTVisitor;
import org.basex.query.value.Value;
import org.basex.query.value.item.QNm;
import org.basex.query.value.node.FElem;
import org.basex.query.var.Var;
import org.basex.util.InputInfo;
import org.basex.util.TokenBuilder;
import org.basex.util.hash.IntObjMap;

/**
 * Function call for user-defined functions.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class StaticFuncCall extends FuncCall {
  /** Static context of this function call. */
  private final StaticContext sc;
  /** Function name. */
  final QNm name;
  /** Function reference. */
  StaticFunc func;

  /**
   * Function call constructor.
   * @param info input info
   * @param name function name
   * @param args arguments
   * @param sc static context
   */
  public StaticFuncCall(final QNm name, final Expr[] args, final StaticContext sc,
      final InputInfo info) {
    this(name, args, sc, null, info);
  }

  /**
   * Copy constructor.
   * @param info input info
   * @param name function name
   * @param args arguments
   * @param sc static context
   * @param func referenced function
   */
  private StaticFuncCall(final QNm name, final Expr[] args, final StaticContext sc,
      final StaticFunc func, final InputInfo info) {
    super(info, args);
    this.sc = sc;
    this.name = name;
    this.func = func;
  }

  @Override
  public Expr compile(final CompileContext cc) throws QueryException {
    super.compile(cc);

    // disallow call of private functions from module with different uri
    if(func.anns.contains(Annotation.PRIVATE) && !func.sc.baseURI().eq(sc.baseURI()))
      throw FUNCPRIVATE_X.get(info, name.string());

    // compile mutually recursive functions
    func.comp(cc);

    // try to inline the function
    final Expr inl = func.inlineExpr(exprs, cc, info);
    if(inl != null) return inl;

    seqType = func.seqType();
    return this;
  }

  @Override
  public StaticFuncCall optimize(final CompileContext cc) {
    // do not inline a static function after compilation as it must be recursive
    return this;
  }

  @Override
  public StaticFuncCall copy(final CompileContext cc, final IntObjMap<Var> vm) {
    final Expr[] args = Arr.copyAll(cc, vm, exprs);
    final StaticFuncCall call = new StaticFuncCall(name, args, sc, func, info);
    call.seqType = seqType;
    call.size = size;
    return call;
  }

  /**
   * Initializes the function and checks for visibility.
   * @param sf function reference
   * @return self reference
   * @throws QueryException query exception
   */
  public StaticFuncCall init(final StaticFunc sf) throws QueryException {
    func = sf;
    if(sf.anns.contains(Annotation.PRIVATE) && !sc.baseURI().eq(sf.sc.baseURI()))
      throw FUNCPRIVATE_X.get(info, sf.name.string());
    return this;
  }

  /**
   * Returns the called function if already known, {@code null} otherwise.
   * @return the function
   */
  public StaticFunc func() {
    return func;
  }

  @Override
  public boolean isVacuous() {
    return func.isVacuous();
  }

  @Override
  public boolean has(final Flag flag) {
    // check arguments, which will be evaluated before running the function code
    if(super.has(flag)) return true;
    // function code: position or context references of expression body have no effect
    if(flag == Flag.POS || flag == Flag.CTX) return false;
    // pass on check to function code
    return flag != Flag.UPD ? func.has(flag) : func.updating();
  }

  @Override
  public void plan(final FElem plan) {
    addPlan(plan, planElem(NAM, name.string(), TCL, tailCall), exprs);
  }

  @Override
  public String description() {
    return FUNC;
  }

  @Override
  public String toString() {
    return new TokenBuilder(name.prefixId()).add(toString(SEP)).toString();
  }

  @Override
  public boolean accept(final ASTVisitor visitor) {
    return visitor.staticFuncCall(this) && super.accept(visitor);
  }

  @Override
  public StaticFunc evalFunc(final QueryContext qc) {
    return func;
  }

  @Override
  Value[] evalArgs(final QueryContext qc) throws QueryException {
    final int al = exprs.length;
    final Value[] args = new Value[al];
    for(int a = 0; a < al; ++a) args[a] = qc.value(exprs[a]);
    return args;
  }
}
