/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.beans;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-7-18
 */
/**
 * $Log: FCDAModel.java,v $
 * Revision 1.1  2013/03/29 09:37:14  cchun
 * Add:创建
 *
 * Revision 1.1  2011/07/19 05:41:45  cchun
 * Add:excel导出java bean
 *
 */
public class FCDAModel {
	
	private String doName;
	private String daName;
	private String desc;
	
	public FCDAModel(String doName,String daName, String desc) {
		this.doName = doName;
		this.daName = daName;
		this.desc = desc;
	}
	
	public String getDoName() {
		return doName;
	}
	public void setDoName(String doName) {
		this.doName = doName;
	}
	public String getDaName() {
		return daName;
	}
	public void setDaName(String daName) {
		this.daName = daName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
