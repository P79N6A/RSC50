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
 * $Log: DescEntry.java,v $
 * Revision 1.1  2013/03/29 09:35:26  cchun
 * Add:创建
 *
 * Revision 1.1  2009/08/27 02:22:40  cchun
 * Refactor:重构导航树模型包路径
 *
 * Revision 1.1  2009/07/16 06:19:42  pht
 * DOI实例的数据描述层级。
 *
 */
public class DescEntry extends TreeEntryImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DescEntry() {
	}
	
	public DescEntry(String name) {
		super(name);
	}

}
