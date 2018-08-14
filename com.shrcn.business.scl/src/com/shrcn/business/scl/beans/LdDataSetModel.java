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
 * $Log: LdDataSetModel.java,v $
 * Revision 1.1  2013/03/29 09:37:12  cchun
 * Add:创建
 *
 * Revision 1.1  2011/07/19 05:41:44  cchun
 * Add:excel导出java bean
 *
 */
public class LdDataSetModel extends LDeviceModel {

	private String name;
	private String desc;
	private List<FCDAModel> fcdas = new ArrayList<FCDAModel>();
	
	public LdDataSetModel(String apName, String ldInst, String name, String desc) {
		super(apName, ldInst);
		this.name = name;
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<FCDAModel> getFcdas() {
		return fcdas;
	}

	public void setFcdas(List<FCDAModel> fcdas) {
		this.fcdas = fcdas;
	}
}
