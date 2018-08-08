/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.tool.rtu.model;

/**
 * 
 * @author 曹兴春(mailto:cxc14375@shrcn.com)
 * @version 1.0, 2013-11-4
 */
/**
 * $Log: TBayInfo.java,v $
 * Revision 1.1  2013/11/05 09:37:31  cxc
 * Add：新建
 *
 */
public class TBayInfo {

	private int id;
	private String oid;
	private String bayName;

	
	
	public TBayInfo(){}
	
	public TBayInfo(String oid, String bayName) {
		super();
		this.oid = oid;
		this.bayName = bayName;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getBayName() {
		return bayName;
	}

	public void setBayName(String bayName) {
		this.bayName = bayName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
