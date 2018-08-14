/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
/**
 * 
 */
package com.shrcn.business.scl.filter.impl;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.shrcn.business.scl.SCTProperties;
import com.shrcn.business.scl.filter.Filter;

/**
 * 
 * @author zhouhuiming(mailto:zhm.3119@shrcn.com)
 * @version 1.0, 2010-6-23
 */
/**
 * $Log: OIDFilter.java,v $
 * Revision 1.1  2013/03/29 09:38:10  cchun
 * Add:创建
 *
 * Revision 1.3  2011/10/21 08:24:09  cchun
 * Fix Bug:修复OIDFilter使用错误
 *
 * Revision 1.2  2011/10/19 06:59:03  cchun
 * Update:优化效率
 *
 * Revision 1.1  2010/09/14 08:10:55  cchun
 * Refactor:添加filter包
 *
 * Revision 1.1  2010/06/28 02:51:14  cchun
 * Update:添加oid过滤功能
 *
 */
public class OIDFilter implements Filter<Object> {

	@Override
	public void filter(Object t) {
		if (!(t instanceof Document))
			return;
		boolean generateOID = SCTProperties.getInstance().generateOID();
		if (generateOID)
			return;
		Element root = ((Document) t).getRootElement().element("Substation");
		if (root == null)
			return;
		List<?> lstEle = root.elements();
		loopElementTree(lstEle, root);
	}

	/**
	 * 循环过滤oid
	 * @param lstElement
	 * @param root
	 */
	private static void loopElementTree(List<?> lstElement,Element root){
		for (Object obj : lstElement) {
			Element ele = (Element) obj;
			List<?> lstEle = ele.elements();
			if ("Private".equalsIgnoreCase(ele.getName())
					&& "oid".equalsIgnoreCase(ele.attributeValue("type"))) {
				if (root.remove(ele)) {
				}
			}
			if (lstEle != null)
				loopElementTree(lstEle, ele);
		}
	}
}
