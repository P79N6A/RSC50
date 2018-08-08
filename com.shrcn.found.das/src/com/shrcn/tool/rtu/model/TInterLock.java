/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.tool.rtu.model;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2014-4-15
 */
/**
 * $Log$
 */
public class TInterLock extends RowData {

	private static final long serialVersionUID = 1L;

	private int id;
	private int pointnum;
	private TDcaCo ctrlp;
	private TDcaSt lockp;
	private String rule;
	
	public TInterLock() {
	}
	
	public String getRef() {
		if (ctrlp == null) {
			return "";
		}
		return ctrlp.getLdref();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getPointnum() {
		return pointnum;
	}

	public void setPointnum(int pointnum) {
		this.pointnum = pointnum;
	}

	public TDcaCo getCtrlp() {
		return ctrlp;
	}

	public void setCtrlp(TDcaCo ctrlp) {
		this.ctrlp = ctrlp;
	}

	public TDcaSt getLockp() {
		return lockp;
	}

	public void setLockp(TDcaSt lockp) {
		this.lockp = lockp;
	}

	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
}
