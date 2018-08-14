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
 * @version 1.0, 2011-7-19
 */
/**
 * $Log: BayLNodeModel.java,v $
 * Revision 1.1  2013/03/29 09:37:10  cchun
 * Add:创建
 *
 * Revision 1.1  2011/07/20 05:51:08  cchun
 * Add:增加excel导出java bean
 *
 */
public class BayLNodeModel {

	private String name;
	private String voltage;
	private List<LNodeModel> lnodes = new ArrayList<LNodeModel>();
	
	public BayLNodeModel(String name, String voltage) {
		this.name = name;
		this.voltage = voltage;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVoltage() {
		return voltage;
	}
	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}
	public List<LNodeModel> getLnodes() {
		return lnodes;
	}
	public void setLnodes(List<LNodeModel> lnodes) {
		this.lnodes = lnodes;
	}
	
}
