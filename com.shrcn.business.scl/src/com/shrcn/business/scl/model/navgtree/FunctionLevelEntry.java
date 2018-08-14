/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.navgtree;

/**
 * 
 * @author 普洪涛(mailto:pht@shrcn.com)
 * @version 1.0, 2009-5-22
 */
/*
 * 修改历史
 * $Log: FunctionLevelEntry.java,v $
 * Revision 1.1  2013/03/29 09:35:21  cchun
 * Add:创建
 *
 * Revision 1.1  2009/08/27 02:22:25  cchun
 * Refactor:重构导航树模型包路径
 *
 * Revision 1.1  2009/05/23 12:25:12  pht
 * 加属性窗口用的“功能”结点。
 *
 */
public class FunctionLevelEntry extends TreeEntryImpl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FunctionLevelEntry(){}

	public FunctionLevelEntry(String name, String xpath, String iconName,int priority) {
		super(name, xpath, iconName, priority);
	}	
}
