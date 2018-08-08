/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.ftu.model;

 /**
 * 
 * @author 李蒙龙(mailto:202137/siyuan.com)
 * @version 1.0, 2016-12-24
 */
public class FourYao {
	protected int id;
	protected int seqNum;
	protected String dbType;
	protected String sAddr;
	protected IedObject iedObject;
	
	public FourYao() {
		super();
	}
	public FourYao(int id) {
		super();
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getsAddr() {
		return sAddr;
	}
	public void setsAddr(String sAddr) {
		this.sAddr = sAddr;
	}
	
	public String getSAddr() {
		return sAddr;
	}
	public void setSAddr(String sAddr) {
		this.sAddr = sAddr;
	}
	
	public IedObject getIedObject() {
		return iedObject;
	}
	
	public void setIedObject(IedObject iedObject) {
		this.iedObject = iedObject;
	}
}

