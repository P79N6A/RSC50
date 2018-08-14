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
 * $Log: ExtRefModel.java,v $
 * Revision 1.1  2013/03/29 09:37:14  cchun
 * Add:创建
 *
 * Revision 1.1  2011/07/20 05:51:09  cchun
 * Add:增加excel导出java bean
 *
 */
public class ExtRefModel {
	private String iedName;
	private String extAddr;
	private String extDesc;
	private String intAddr;
	private String intDesc;
	
	public ExtRefModel(String iedName, String extAddr, String extDesc, String intAddr, String intDesc) {
		this.iedName = iedName;
		this.extAddr = extAddr;
		this.extDesc = extDesc;
		this.intAddr = intAddr;
		this.intDesc = intDesc;
	}

	public String getIedName() {
		return iedName;
	}

	public void setIedName(String iedName) {
		this.iedName = iedName;
	}

	public String getExtAddr() {
		return extAddr;
	}

	public void setExtAddr(String extAddr) {
		this.extAddr = extAddr;
	}

	public String getExtDesc() {
		return extDesc;
	}

	public void setExtDesc(String extDesc) {
		this.extDesc = extDesc;
	}

	public String getIntAddr() {
		return intAddr;
	}

	public void setIntAddr(String intAddr) {
		this.intAddr = intAddr;
	}

	public String getIntDesc() {
		return intDesc;
	}

	public void setIntDesc(String intDesc) {
		this.intDesc = intDesc;
	}
	
}
