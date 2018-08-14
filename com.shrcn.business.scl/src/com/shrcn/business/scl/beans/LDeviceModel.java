/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-7-18
 */
/**
 * $Log: LDeviceModel.java,v $
 * Revision 1.1  2013/03/29 09:37:12  cchun
 * Add:创建
 *
 * Revision 1.1  2011/07/19 05:41:44  cchun
 * Add:excel导出java bean
 *
 */
public class LDeviceModel {
	
	private String apName;
	private String ldInst;
	private List<LdLnModel> lns = new ArrayList<LdLnModel>();
	
	public LDeviceModel(String apName, String ldInst) {
		this.apName = apName;
		this.ldInst = ldInst;
	}
	
	public String getApName() {
		return apName;
	}
	public void setApName(String apName) {
		this.apName = apName;
	}
	public String getLdInst() {
		return ldInst;
	}
	public void setLdInst(String ldInst) {
		this.ldInst = ldInst;
	}

	public List<LdLnModel> getLns() {
		return lns;
	}

	public void setLns(List<LdLnModel> lns) {
		this.lns = lns;
	}
}
