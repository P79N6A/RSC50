/**
 * Copyright (c) 2007-2016 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.ftu.model;

 /**
 * 
 * @author 王敏(mailto:wm.202039@sieyuan.com)
 * @version 1.0, 2017-2-18
 */
public class PointGroup {

	private int id;
	private String desc;
	private String otherName;
	private String type;
	private String expr;
	private String dataId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getOtherName() {
		return otherName;
	}
	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getExpr() {
		return expr;
	}
	public void setExpr(String expr) {
		this.expr = expr;
	}

	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	
	@Override
	public String toString() {
		return "PointGroup [id=" + id + ", desc=" + desc + ", otherName="
				+ otherName + ", type=" + type + ", expr=" + expr + ", dataId="
				+ dataId + "]";
	}
}

