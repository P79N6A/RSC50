/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.rtu.model;

/**
 * 实时点模型
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-3-22
 */
/**
 * $Log: SysPoint.java,v $
 * Revision 1.1  2012/11/26 08:37:13  cchun
 * Refactor:提取公共方法
 *
 * Revision 1.1  2011/03/22 08:52:29  cchun
 * Add:点信息实体类
 *
 */
public class SysPoint {
	private String fc;
	private String sAddr;
	
	public SysPoint(String fc, String sAddr) {
		this.fc = fc;
		this.sAddr = sAddr;
	}
	
	public String getFc() {
		return fc;
	}
	public void setFc(String fc) {
		this.fc = fc;
	}
	public String getSAddr() {
		return sAddr;
	}
	public void setSAddr(String addr) {
		sAddr = addr;
	}
	
	@Override
	public String toString() {
		return fc + sAddr;
	}
}
