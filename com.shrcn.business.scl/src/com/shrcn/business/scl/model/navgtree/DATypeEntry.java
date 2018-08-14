/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.model.navgtree;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-12-24
 */
/**
 * $Log: DATypeEntry.java,v $
 * Revision 1.1  2013/03/29 09:35:23  cchun
 * Add:创建
 *
 * Revision 1.1  2010/12/24 03:46:46  cchun
 * Add:添加数据模板节点类
 *
 */
public class DATypeEntry extends TreeEntryImpl {

	private static final long serialVersionUID = 1L;
	
	public DATypeEntry(){}

	public DATypeEntry(String name, String toolTip, String iconName) {
		super(name, null, iconName, toolTip, null);
	}

}
