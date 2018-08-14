/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.model;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-11-10
 */
/**
 * $Log: OutRelation.java,v $
 * Revision 1.1  2013/03/29 09:36:39  cchun
 * Add:创建
 *
 * Revision 1.2  2010/05/20 10:34:18  cchun
 * Update:增加获取显示名方法
 *
 * Revision 1.1  2009/11/13 07:18:13  cchun
 * Update:完善关联表格功能
 *
 */
public class OutRelation extends Relation {
	
	public static final String DOT = ".";
	public static final String DIVISION = "/";
	
//	private String extIEDDesc = null;
	private String extDODesc = null;
	private String extRef = null;
//	private String extIEDName = null;
//	private String extLdInst = null;
//	private String extPrefix = null;
//	private String extLnClass = null;
//	private String extLnInst = null;
//	private String extDoName = null;
//	private String extDaName = null;
	private String inIEDName = null;
	private String inIEDDesc = null;
	private String intAddr = null;
	
	public String getIntAddr() {
		return intAddr;
	}
	public void setIntAddr(String intAddr) {
		this.intAddr = intAddr;
	}
//	public String getDOExtRef() {
//		return extIEDName + extLdInst + DIVISION + 
//			extPrefix + DOT + extLnClass + DOT + extLnInst + DOT + extDoName;
//	}
	public String getExtRef() {
		return extRef;
	}
//	public String getExtIEDName() {
//		return extIEDName;
//	}
//	public void setExtIEDName(String extIEDName) {
//		this.extIEDName = extIEDName;
//	}
//	public String getExtLdInst() {
//		return extLdInst;
//	}
//	public void setExtLdInst(String extLdInst) {
//		this.extLdInst = extLdInst;
//	}
//	public String getExtPrefix() {
//		return extPrefix;
//	}
//	public void setExtPrefix(String extPrefix) {
//		this.extPrefix = extPrefix;
//	}
//	public String getExtLnClass() {
//		return extLnClass;
//	}
//	public void setExtLnClass(String extLnClass) {
//		this.extLnClass = extLnClass;
//	}
//	public String getExtLnInst() {
//		return extLnInst;
//	}
//	public void setExtLnInst(String extLnInst) {
//		this.extLnInst = extLnInst;
//	}
//	public String getExtDoName() {
//		return extDoName;
//	}
//	public void setExtDoName(String extDoName) {
//		this.extDoName = extDoName;
//	}
//	public String getExtDaName() {
//		return extDaName;
//	}
//	public void setExtDaName(String extDaName) {
//		this.extDaName = extDaName;
//	}
//	public String getExtIEDDesc() {
//		return extIEDDesc;
//	}
//	public void setExtIEDDesc(String extIEDDesc) {
//		this.extIEDDesc = extIEDDesc;
//	}
	public String getExtDODesc() {
		return extDODesc;
	}
	public void setExtDODesc(String extDODesc) {
		this.extDODesc = extDODesc;
	}
	public void setExtRef(String extRef) {
		this.extRef = extRef;
	}
	public String getInIEDName() {
		return inIEDName;
	}
	
	/**
	 * 获取IED显示名
	 * @return
	 */
	public String getDisplayInIEDName() {
		return (inIEDName==null?"":inIEDName) + 
			("".equals(inIEDDesc)||inIEDDesc==null ? "" : ":" + inIEDDesc);
	}
	
	public void setInIEDName(String inIEDName) {
		this.inIEDName = inIEDName;
	}
	public String getInIEDDesc() {
		return inIEDDesc;
	}
	public void setInIEDDesc(String inIEDDesc) {
		this.inIEDDesc = inIEDDesc;
	}
}
