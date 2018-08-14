/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.navgtree;

/**
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-5-15
 */
/*
 * 修改历史
 * $Log: TerminalEntry.java,v $
 * Revision 1.1  2013/03/29 09:35:22  cchun
 * Add:创建
 *
 * Revision 1.1  2009/08/27 02:22:35  cchun
 * Refactor:重构导航树模型包路径
 *
 * Revision 1.2  2009/05/22 03:03:58  lj6061
 * 修改节点属性添加优先级
 *
 * Revision 1.1  2009/05/18 07:08:12  lj6061
 * 导入1次信息
 *
 */
public class TerminalEntry extends TreeEntryImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TerminalEntry(){}

	public TerminalEntry(String name, String xpath, String iconName,int priority) {
		super(name, xpath, iconName, priority);
	}

}
