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
 * $Log: DataTemplatesEntry.java,v $
 * Revision 1.1  2013/03/29 09:35:24  cchun
 * Add:创建
 *
 * Revision 1.2  2010/12/29 06:44:07  cchun
 * Update:添加DataTemplatesEntry()
 *
 * Revision 1.1  2010/12/24 03:46:45  cchun
 * Add:添加数据模板节点类
 *
 */
public class DataTemplatesEntry extends TreeEntryImpl {

	private static final long serialVersionUID = 1L;
	
	public DataTemplatesEntry() {}

	public DataTemplatesEntry(String name, String xpath, String iconName,
			String toolTip) {
		super(name, xpath, iconName, toolTip, null);
	}
}