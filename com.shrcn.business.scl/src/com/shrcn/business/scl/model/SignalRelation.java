/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.model;

import com.shrcn.found.common.util.StringUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-11-10
 */
/**
 * $Log: SignalRelation.java,v $
 * Revision 1.2  2013/08/05 07:57:01  cchun
 * Update:增加对daName为空的判断
 *
 * Revision 1.1  2013/03/29 09:36:37  cchun
 * Add:创建
 *
 * Revision 1.1  2010/09/27 07:08:24  cchun
 * Refactor:改名
 *
 * Revision 1.4  2010/05/20 10:34:18  cchun
 * Update:增加获取显示名方法
 *
 * Revision 1.3  2009/11/13 09:38:40  cchun
 * Update:增加开入端子描述
 *
 * Revision 1.2  2009/11/13 08:03:07  cchun
 * Fix Bug:修改参引格式的bug
 *
 * Revision 1.1  2009/11/13 07:18:13  cchun
 * Update:完善关联表格功能
 *
 */
public class SignalRelation extends Relation {
	
	public static final String DOT = ".";
	public static final String DIVISION = "/";
	
	private String inIEDName = null;
	private String inIEDDesc = null;
	private String intAddr = null;
	private String intAddrDesc = null;
	private String extIEDDesc = null;
	private String extDODesc = null;
//	private String extRef = null;
	private String extIEDName = null;
	private String extLdInst = null;
	private String extPrefix = null;
	private String extLnClass = null;
	private String extLnInst = null;
	private String extDoName = null;
	private String extDaName = null;
	
	public String getIntAddr() {
		return intAddr;
	}
	public void setIntAddr(String intAddr) {
		this.intAddr = intAddr;
	}
	public String getDOExtRef() {
		return extIEDName + extLdInst + DIVISION + 
			extPrefix + extLnClass + extLnInst + DOT + extDoName;
	}
	public String getExtRef() {
		return (extIEDName==null) ? null : (extIEDName + extLdInst + DIVISION + 
			extPrefix + extLnClass + extLnInst + DOT + extDoName + 
			(StringUtil.isEmpty(extDaName) ? "" : (DOT + extDaName)));
	}
	public String getExtIEDName() {
		return extIEDName;
	}
	
	/**
	 * 获取IED显示名
	 * @return
	 */
	public String getDisplayIEDName() {
		return (extIEDName==null?"":extIEDName) + 
		("".equals(extIEDDesc)||extIEDDesc==null ? "" : ":" + extIEDDesc);
	}
	
	public void setExtIEDName(String extIEDName) {
		this.extIEDName = extIEDName;
	}
	public String getExtLdInst() {
		return extLdInst;
	}
	public void setExtLdInst(String extLdInst) {
		this.extLdInst = extLdInst;
	}
	public String getExtPrefix() {
		return extPrefix;
	}
	public void setExtPrefix(String extPrefix) {
		this.extPrefix = extPrefix;
	}
	public String getExtLnClass() {
		return extLnClass;
	}
	public void setExtLnClass(String extLnClass) {
		this.extLnClass = extLnClass;
	}
	public String getExtLnInst() {
		return extLnInst;
	}
	public void setExtLnInst(String extLnInst) {
		this.extLnInst = extLnInst;
	}
	public String getExtDoName() {
		return extDoName;
	}
	public void setExtDoName(String extDoName) {
		this.extDoName = extDoName;
	}
	public String getExtDaName() {
		return extDaName;
	}
	public void setExtDaName(String extDaName) {
		this.extDaName = extDaName;
	}
	public String getExtIEDDesc() {
		return extIEDDesc;
	}
	public void setExtIEDDesc(String extIEDDesc) {
		this.extIEDDesc = extIEDDesc;
	}
	public String getExtDODesc() {
		return extDODesc;
	}
	public void setExtDODesc(String extDODesc) {
		this.extDODesc = extDODesc;
	}
	public String getIntAddrDesc() {
		return intAddrDesc;
	}
	public void setIntAddrDesc(String intAddrDesc) {
		this.intAddrDesc = intAddrDesc;
	}
	public String getInIEDName() {
		return inIEDName;
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
	/**
	 * 获取IED显示名
	 * @return
	 */
	public String getDisplayInIEDName() {
		return (inIEDName==null?"":inIEDName) + 
			("".equals(inIEDDesc)||inIEDDesc==null ? "" : ":" + inIEDDesc);
	}
}
