/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.navgtree;

/**
 * 
 * @author 黄钦辉(mailto:huangqinhui@shrcn.com)
 * @version 1.0, 2009-4-23
 */
/*
 * 修改历史
 * $Log: HistoryEntry.java,v $
 * Revision 1.1  2013/03/29 09:35:24  cchun
 * Add:创建
 *
 * Revision 1.1  2009/08/27 02:22:33  cchun
 * Refactor:重构导航树模型包路径
 *
 * Revision 1.1  2009/04/23 07:59:37  hqh
 * 添加节点实例子
 *
 */
public class HistoryEntry extends TreeEntryImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public HistoryEntry(){}

	public HistoryEntry(String name, String xpath, String iconName,
			String toolTip, String editorID) {
		super(name, xpath, iconName, toolTip, editorID);
	}

}
