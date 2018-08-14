/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.navgtree;

/**
 * 
 * @author 普洪涛(mailto:pht@shrcn.com)
 * @version 1.0, 2009-7-14
 */
/*
 * 修改历史
 * $Log: LNInfo.java,v $
 * Revision 1.1  2013/03/29 09:35:21  cchun
 * Add:创建
 *
 * Revision 1.4  2010/10/12 01:44:28  cchun
 * Update:增加构造函数、toString()
 *
 * Revision 1.3  2010/09/14 08:17:22  cchun
 * Update:整理格式
 *
 * Revision 1.2  2010/06/18 09:50:12  cchun
 * Update:添加描述导出、导入功能
 *
 * Revision 1.1  2009/08/27 02:22:42  cchun
 * Refactor:重构导航树模型包路径
 *
 * Revision 1.1  2009/07/16 06:20:55  pht
 * DOI实例的LN信息的模型类。
 *
 */
public class LNInfo {
	public String lnXpath;
	private String prefix;
	private String lnClass;
	private String inst;
	
	public LNInfo() {
	}

	public LNInfo(String prefix, String lnClass, String inst) {
		this.prefix = prefix;
		this.lnClass = lnClass;
		this.inst = inst;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getLnClass() {
		return lnClass;
	}

	public void setLnClass(String lnClass) {
		this.lnClass = lnClass;
	}

	public String getInst() {
		return inst;
	}

	public void setInst(String inst) {
		this.inst = inst;
	}

	@Override
	public String toString() {
		return prefix + lnClass + inst;
	}
}
