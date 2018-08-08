/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.treetable;

import org.dom4j.Element;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-9-1
 */
/**
 * $Log: ElementTableAdapter.java,v $
 * Revision 1.1  2013/03/29 09:36:44  cchun
 * Add:创建
 *
 * Revision 1.1  2011/09/02 07:09:33  cchun
 * Add:XML节点对象Adapter
 *
 */
public class ElementTableAdapter extends DefaultTreeTableAdapter {
	public static ElementTableAdapter instance = new ElementTableAdapter();

	private ElementTableAdapter() {
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Element
				&& ((Element) parentElement).elements() != null
				&& parentElement != null)
			return ((Element) parentElement).elements().toArray();
		else
			return new Object[0];

	}
}