package org.basex.io.serial.dot;

import static org.basex.core.Text.NL;

import org.basex.query.expr.And;
import org.basex.query.expr.Arith;
import org.basex.query.expr.Cast;
import org.basex.query.expr.Catch;
import org.basex.query.expr.CmpG;
import org.basex.query.expr.CmpN;
import org.basex.query.expr.CmpR;
import org.basex.query.expr.CmpV;
import org.basex.query.expr.ContextValue;
import org.basex.query.expr.Except;
import org.basex.query.expr.If;
import org.basex.query.expr.InterSect;
import org.basex.query.expr.List;
import org.basex.query.expr.Or;
import org.basex.query.expr.Pos;
import org.basex.query.expr.Preds;
import org.basex.query.expr.Quantifier;
import org.basex.query.expr.Range;
import org.basex.query.expr.Root;
import org.basex.query.expr.Try;
import org.basex.query.expr.Union;
import org.basex.query.expr.constr.CAttr;
import org.basex.query.expr.constr.CComm;
import org.basex.query.expr.constr.CDoc;
import org.basex.query.expr.constr.CElem;
import org.basex.query.expr.constr.CNSpace;
import org.basex.query.expr.constr.CPI;
import org.basex.query.expr.constr.CTxt;
import org.basex.query.expr.ft.FTContains;
import org.basex.query.expr.ft.FTExpr;
import org.basex.query.expr.ft.FTIndexAccess;
import org.basex.query.expr.gflwor.For;
import org.basex.query.expr.gflwor.GFLWOR;
import org.basex.query.expr.gflwor.Let;
import org.basex.query.expr.gflwor.OrderBy;
import org.basex.query.expr.index.RangeAccess;
import org.basex.query.expr.index.StringRangeAccess;
import org.basex.query.expr.index.ValueAccess;
import org.basex.query.expr.path.Path;
import org.basex.query.func.StandardFunc;
import org.basex.query.func.StaticFunc;
import org.basex.query.func.StaticFuncCall;
import org.basex.query.func.StaticFuncs;
import org.basex.query.value.item.Int;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.QNm;
import org.basex.query.value.item.Str;
import org.basex.query.value.item.Uri;
import org.basex.query.value.seq.Seq;
import org.basex.query.var.StaticVar;
import org.basex.query.var.Var;
import org.basex.query.var.VarRef;
import org.basex.query.var.VarStack;
import org.basex.util.Token;
import org.basex.util.Util;

/**
 * This class contains formatting information for the DOT output.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
final class DOTData {
  /** Font. */
  private static final String FONT = "Tahoma"; //"Charter BT";

  /** Node entry. */
  static final String HEADER =
    "digraph BaseXAlgebra {" + NL +
    "node [shape=box style=bold width=0 height=0];" + NL +
    "node [fontsize=12 fontname=\"" + FONT + "\"];";
  /** Node entry. */
  static final String FOOTER = "}";

  /** Node entry. */
  static final String DOTNODE = "node% [label=\"%\" color=\"#%\"];";
  /** Link entry. */
  static final String DOTLINK = "node% -> node%;";
  /** Node entry. */
  static final String DOTATTR = "\\n%: %";

  /** Link entry. */
  static final String ELEM1 = "303030";
  /** Link entry. */
  static final String ELEM2 = "909090";
  /** Link entry. */
  static final String ITEM = "3366CC";
  /** Link entry. */
  static final String TEXT = "6666FF";
  /** Link entry. */
  static final String COMM = "3366FF";
  /** Link entry. */
  static final String PI = "3399FF";

  /** Private constructors. */
  private DOTData() { }

  /** Hash map, caching colors for expressions. */
  private static final Object[][] COLORS = {
    // blue
    { "6666FF", Item.class, Seq.class, Str.class, QNm.class, Uri.class, Int.class },
    // violet
    { "9933FF", CAttr.class, CComm.class, CDoc.class, CElem.class,
                CNSpace.class, CPI.class, CTxt.class },
    { "9933CC", And.class, Or.class, Union.class, InterSect.class, Except.class },
    // pink
    { "CC3399", If.class, Quantifier.class },
    { "CC6699", OrderBy.class },
    // red
    { "FF3333", Arith.class, CmpG.class, CmpN.class, CmpV.class, CmpR.class,
                Pos.class, FTContains.class },
    { "FF6666", FTExpr.class, Try.class, Catch.class },
    // orange
    { "AA9988", StaticFunc.class },
    { "776655", StaticFuncs.class },
    { "CC6600", Path.class },
    { "FF9900", Preds.class },
    // green
    { "009900", GFLWOR.class },
    { "339933", VarStack.class },
    { "33CC33", For.class, Let.class, List.class, Range.class, ContextValue.class },
    { "66CC66", Var.class, Cast.class },
    // cyan
    { "009999", StaticFuncCall.class, StandardFunc.class, Root.class, VarRef.class,
                StaticVar.class, ValueAccess.class, RangeAccess.class,
                StringRangeAccess.class, FTIndexAccess.class },
  };

  /**
   * Returns the color for the specified string or {@code null}.
   * @param string string string
   * @return color
   */
  static String color(final byte[] string) {
    for(final Object[] color : COLORS) {
      final int cl = color.length;
      for(int c = 1; c < cl; c++) {
        final Object o = color[c];
        final byte[] col = o instanceof byte[] ? (byte[]) o :
          Token.token(o instanceof Class ? Util.className((Class<?>) o) : o.toString());
        if(Token.eq(col, string)) return color[0].toString();
      }
    }
    return null;
  }
}
