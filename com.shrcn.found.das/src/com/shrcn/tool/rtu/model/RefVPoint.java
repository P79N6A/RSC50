/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */

package com.shrcn.tool.rtu.model;


/**
 * 虚点引用数据对象.
 *
 * @author 孙春颖
 * @version 1.0, Jan 22, 2013
 */
 /**
 * $$Log: RefVPoint.java,v $
 * $Revision 1.2  2013/04/03 03:17:05  scy
 * $Update：将reftype参数修改为int型
 * $
 * $Revision 1.1  2013/01/23 08:55:41  scy
 * $Add:虚点引用对象
 * $$
 */
public class RefVPoint extends RefPoint {

	private int pointid;
	private String pointtype;
	private String dbtype;
	private int seqnum;

	public RefVPoint(int refid, int reftype) {
		setRefid(refid);
		setReftype(reftype);
	}
	
	public RefVPoint(int refid, int reftype, String pointdesc, String pointtype, String dbtype, int seqnum) {
		this(refid, reftype);
		setPointdesc(pointdesc);
		this.pointtype = pointtype;
		this.dbtype = dbtype;
		this.seqnum = seqnum;
	}

	public int getPointid() {
		return pointid;
	}

	public void setPointid(int pointid) {
		this.pointid = pointid;
	}

	public String getPointtype() {
		return pointtype;
	}

	public void setPointtype(String pointtype) {
		this.pointtype = pointtype;
	}

	public String getDbtype() {
		return dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	public int getSeqnum() {
		return seqnum;
	}

	public void setSeqnum(int seqnum) {
		this.seqnum = seqnum;
	}
	
}
