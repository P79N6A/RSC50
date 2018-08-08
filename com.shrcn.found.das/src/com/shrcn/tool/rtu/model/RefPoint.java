/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.rtu.model;



/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-1-4
 */
/**
 * $Log: RefPoint.java,v $
 * Revision 1.5  2013/10/08 06:31:00  cxc
 * update:增加104采集点引用信息
 *
 * Revision 1.4  2013/05/16 06:03:24  dlh
 * 解决在删除IED时该IED被引用列表框内IED装置名称显示两次的问题
 *
 * Revision 1.3  2013/04/03 05:23:45  scy
 * Update：将reftype属性修改为int型，同时对应修改getCategory及getDpaPoint方法实现
 *
 * Revision 1.2  2013/01/23 08:38:32  scy
 * Add:虚点引用对象
 *
 * Revision 1.1  2013/01/05 12:15:55  cchun
 * Add:引用操作类
 *
 */
public class RefPoint {
	
	private int iedid; 
	private String iedname; 
	private String ieddesc; 
	private String ldname; 
	private String pointdesc; 
	private String pointref; 
	private int refid;
	private int reftype;
	private int index;
	private int pointNum;
	private String adiotype;
	
	public RefPoint() {}
	
	public RefPoint(int iedid, String iedname, String ieddesc, String ldname,
			String pointdesc, String pointref, int refid, int reftype) {
		super();
		this.iedid = iedid;
		this.iedname = iedname;
		this.ieddesc = ieddesc;
		this.ldname = ldname;
		this.pointdesc = pointdesc;
		this.pointref = pointref;
		this.refid = refid;
		this.reftype = reftype;
	}
	
	public RefPoint(int iedid, String iedname, String ieddesc, 
			String pointdesc, int refid, int reftype) {
		super();
		this.iedid = iedid;
		this.iedname = iedname;
		this.ieddesc = ieddesc;
		this.pointdesc = pointdesc;
		this.refid = refid;
		this.reftype = reftype;
	}

	public RefPoint(int iedid, String iedname, int pointNum, 
			String pointdesc, String adiotype ,int refid, int reftype) {
		super();
		this.iedid = iedid;
		this.iedname = iedname;
		this.pointNum = pointNum;
		this.pointdesc = pointdesc;
		this.adiotype = adiotype;
		this.refid = refid;
		this.reftype = reftype;
	}
	public int getIedid() {
		return iedid;
	}

	public void setIedid(int iedid) {
		this.iedid = iedid;
	}

	public String getIedname() {
		return iedname;
	}

	public void setIedname(String iedname) {
		this.iedname = iedname;
	}

	public String getIeddesc() {
		return ieddesc;
	}

	public void setIeddesc(String ieddesc) {
		this.ieddesc = ieddesc;
	}

	public String getLdname() {
		return ldname;
	}

	public void setLdname(String ldname) {
		this.ldname = ldname;
	}

	public String getPointdesc() {
		return pointdesc;
	}

	public void setPointdesc(String pointdesc) {
		this.pointdesc = pointdesc;
	}

	public String getPointref() {
		return pointref;
	}

	public void setPointref(String pointref) {
		this.pointref = pointref;
	}

	public int getRefid() {
		return refid;
	}

	public void setRefid(int refid) {
		this.refid = refid;
	}

	public int getReftype() {
		return reftype;
	}

	public void setReftype(int reftype) {
		this.reftype = reftype;
	}
	
	public String getRef() {
		if (ldname == null)
			return "";
		return ldname + "/" + pointref;
	}
	
	public int getIndex(){
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getPointNum() {
		return pointNum;
	}

	public void setPointNum(int pointNum) {
		this.pointNum = pointNum;
	}

	public String getAdiotype() {
		return adiotype;
	}

	public void setAdiotype(String adiotype) {
		this.adiotype = adiotype;
	}
	
	public String getCategory(){
		return EnumRef.getCategory(reftype);
	}
}
