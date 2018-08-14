/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.das;

import com.shrcn.business.scl.model.SCL;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-12-11
 */
/**
 * $Log: IEDEditDAO.java,v $
 * Revision 1.1  2013/03/29 09:36:18  cchun
 * Add:创建
 *
 * Revision 1.6  2011/09/13 08:05:10  cchun
 * Fix Bug:修复iedName为null的bug
 *
 * Revision 1.5  2010/12/29 06:43:07  cchun
 * Update:修改参数
 *
 * Revision 1.4  2010/12/07 09:37:42  cchun
 * Update:去掉访问点过滤
 *
 * Revision 1.3  2010/05/26 08:10:31  cchun
 * Update:修改构造函数参数
 *
 * Revision 1.2  2010/04/23 03:22:25  cchun
 * Update:历史记录接口添加oid参数
 *
 * Revision 1.1  2009/12/11 05:26:47  cchun
 * Update:添加修改历史标记
 *
 */
public class IEDEditDAO implements SCLDAO {
	
	protected String iedXPath; 
	protected String iedName; 
	protected String ldInst;
	protected String lnName;

	/**
	 * 
	 * @param iedXPath
	 * @param apName
	 * @param ldInst
	 */
	public IEDEditDAO(String iedName, String ldInst) {
		this.iedName = iedName;
		this.ldInst = ldInst;
		this.iedXPath = SCL.getIEDXPath(iedName);
	}
	
	/**
	 * 按ln操作数据类的构造方法
	 * @param iedName
	 * @param apName
	 * @param ldInst
	 * @param lnName
	 */
	public IEDEditDAO(String iedName, String ldInst, String lnName) {
		this(iedName, ldInst);
		this.lnName = lnName;
	}

	public String getIedXPath() {
		return iedXPath;
	}

	public void setIedXPath(String iedXPath) {
		this.iedXPath = iedXPath;
	}

	public String getIedName() {
		return iedName;
	}

	public void setIedName(String iedName) {
		this.iedName = iedName;
	}

	public String getLdInst() {
		return ldInst;
	}

	public void setLdInst(String ldInst) {
		this.ldInst = ldInst;
	}

	public String getLnName() {
		return lnName;
	}

	public void setLnName(String lnName) {
		this.lnName = lnName;
	}
}
