/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.navgtree;

/**
 * 
 * @author 普洪涛(mailto:pht@shrcn.com)
 * @version 1.0, 2009-7-8
 */
/*
 * 修改历史
 * $Log: DAEntry.java,v $
 * Revision 1.1  2013/03/29 09:35:26  cchun
 * Add:创建
 *
 * Revision 1.2  2010/12/24 03:46:59  cchun
 * Update:增加构造方法
 *
 * Revision 1.1  2009/08/27 02:22:32  cchun
 * Refactor:重构导航树模型包路径
 *
 * Revision 1.1  2009/07/16 06:19:24  pht
 * DOI实例的DA层级。
 *
 */
public class DAEntry extends TreeEntryImpl {

	private static final long serialVersionUID = 1L;
	
	public DAEntry(){}

	public DAEntry(String name) {
		super(name);
	}

	public DAEntry(String name, String toolTip, String iconName) {
		super(name, null, iconName, toolTip, null);
	}
}
