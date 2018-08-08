/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.util;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-9-10
 */
/**
 * $Log: XPathUtil.java,v $
 * Revision 1.1  2013/03/29 09:36:27  cchun
 * Add:创建
 *
 * Revision 1.3  2012/03/22 03:06:12  cchun
 * Update:添加getNodeName()
 *
 * Revision 1.2  2012/03/21 00:56:22  cchun
 * Fix Bug:修复getParentName()与新的xpath形式不匹配的bug
 *
 * Revision 1.1  2010/09/13 09:00:37  cchun
 * Add:xpath工具类
 *
 */
public class XPathUtil {
	
	private XPathUtil() {}
	
	/**
	 * 获取父节点xpath
	 * @param xpath
	 * @return
	 */
	public static String getParentXPath(String xpath) {
		return xpath.substring(0, xpath.lastIndexOf('/'));
	}
	
	/**
	 * 根据xpath获取父节点名称
	 * @param xpath
	 * @return
	 */
	public static String getParentName(String xpath) {
		String[] nodePathes = xpath.split("/");
		String nodeParent = nodePathes[nodePathes.length - 1];
		return nodeParent.split("'")[1];
	}
	
	/**
	 * 根据xpath获取节点名称
	 * @param xpath
	 * @return
	 */
	public static String getNodeName(String xpath) {
		String[] nodePathes = xpath.split("/");
		String nodeParent = nodePathes[nodePathes.length - 1];
		return nodeParent.split("'")[3];
	}
}
