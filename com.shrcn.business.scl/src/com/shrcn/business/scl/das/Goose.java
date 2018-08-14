/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
/**
 * 
 */
package com.shrcn.business.scl.das;

/**
 * 
 * @author zhouhuiming(mailto:zhm.3119@shrcn.com)
 * @version 1.0, 2010-8-19
 */
/**
 * $Log: Goose.java,v $
 * Revision 1.1  2013/03/29 09:36:19  cchun
 * Add:创建
 *
 * Revision 1.1  2011/01/10 08:39:31  cchun
 * 聂国勇提交，修改信号关联检查功能
 *
 * Revision 1.1  2010/08/24 02:27:48  cchun
 * Update:goose值对象类
 *
 */
public class Goose {
	private String cbName = "";
	private String ldInst = "";
	private String mac = "";
	private String vlanId = "";
	private String vlanP = "";
	private String appId = "";
	private String min = "";
	private String max = "";

	public String getCbName() {
		return cbName;
	}

	public void setCbName(String cbName) {
		this.cbName = cbName;
	}

	public String getLdInst() {
		return ldInst;
	}

	public void setLdInst(String ldInst) {
		this.ldInst = ldInst;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getVlanId() {
		return vlanId;
	}

	public void setVlanId(String vlanId) {
		this.vlanId = vlanId;
	}

	public String getVlanP() {
		return vlanP;
	}

	public void setVlanP(String vlanP) {
		this.vlanP = vlanP;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}
}
