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
 * $Log: CFGParameterEntry.java,v $
 * Revision 1.1  2013/03/29 09:35:23  cchun
 * Add:创建
 *
 * Revision 1.1  2009/08/27 02:22:39  cchun
 * Refactor:重构导航树模型包路径
 *
 * Revision 1.1  2009/07/16 06:19:02  pht
 * DOI实例化的配置参数层级
 *
 */
public class CFGParameterEntry extends TreeEntryImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CFGParameterEntry(){}
	
	public CFGParameterEntry(String name) {
		super(name);
	}

}
