/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.util;

import java.util.List;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-7-18
 */
/**
 * $Log: XQueryUtil.java,v $
 * Revision 1.1  2013/03/29 09:36:29  cchun
 * Add:创建
 *
 * Revision 1.1  2011/08/01 08:18:29  cchun
 * Add:Xquery功能类
 *
 * Revision 1.1  2011/07/19 05:42:36  cchun
 * Add:xquery常量类
 *
 */
public class XQueryUtil {

//	private static final String DECLARE_NS = "declare namespace scl='http://www.iec.ch/61850/2003/SCL'; ";

	public static final String DECLARE_DEFAULT_NS = "declare default element namespace 'http://www.iec.ch/61850/2003/SCL'; "; //$NON-NLS-1$

	public static final String DECLARE_ADD_ATT_FUN = "declare namespace cchun = 'http://www.cchun.com'; " +
		"declare function cchun:add-or-update-attributes ($elements as element()* , $attrNames as xs:QName* , $attrValues as xs:anyAtomicType* ) " +
		"as element()? {for $element in $elements return element { node-name($element)} { $element/@*[not(node-name(.) = $attrNames)], " +
		"for $attrName at $seq in $attrNames return attribute {$attrName}{$attrValues[$seq]},$element/node()}}; ";

	private XQueryUtil() {}
	
	public static String getGroupString(String[] aps) {
		StringBuilder sb = new StringBuilder("(");
		for (String ap : aps) {
			sb.append("'" + ap + "'").append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		return sb.toString();
	}
	
	public static String getGroupString(List<String> aps) {
		return getGroupString(aps.toArray(new String[aps.size()]));
	}
}
