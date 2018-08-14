/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.navgtree;

/**
 * 
 * @author 普洪涛(mailto:pht@shrcn.com)
 * @version 1.0, 2009-7-7
 */
/*
 * 修改历史
 * $Log: LNEntry.java,v $
 * Revision 1.1  2013/03/29 09:35:24  cchun
 * Add:创建
 *
 * Revision 1.3  2011/01/27 01:04:45  cchun
 * Add:聂国勇增加，增加数据模板的详细显示功能
 *
 * Revision 1.2  2010/12/24 03:46:59  cchun
 * Update:增加构造方法
 *
 * Revision 1.1  2009/08/27 02:22:34  cchun
 * Refactor:重构导航树模型包路径
 *
 * Revision 1.1  2009/07/16 06:20:19  pht
 * DOI实例的LN层级。
 *
 */
public class LNEntry extends TreeEntryImpl {

	private static final long serialVersionUID = 1L;
	
	public LNEntry(){}

	public LNEntry(String name) {
		super(name);
	}
	
	public LNEntry(String name, String toolTip, String iconName,String editorID) {
		super(name, null, iconName, toolTip, editorID);
	}
}
