/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.beans;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-7-19
 */
/**
 * $Log: LNodeModel.java,v $
 * Revision 1.1  2013/03/29 09:37:14  cchun
 * Add:创建
 *
 * Revision 1.1  2011/07/20 05:51:09  cchun
 * Add:增加excel导出java bean
 *
 */
public class LNodeModel {
	private String eqp;
	private String iedName;
	private String ldInst;
	private String lnName;
	private String lnDesc;
	
	public LNodeModel(String eqp, String iedName, String ldInst, String lnName, String lnDesc) {
		this.eqp = eqp;
		this.iedName = iedName;
		this.ldInst = ldInst;
		this.lnName = lnName;
		this.lnDesc = lnDesc;
	}

	public String getEqp() {
		return eqp;
	}

	public void setEqp(String eqp) {
		this.eqp = eqp;
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

	public String getLnDesc() {
		return lnDesc;
	}

	public void setLnDesc(String lnDesc) {
		this.lnDesc = lnDesc;
	}
	
}
