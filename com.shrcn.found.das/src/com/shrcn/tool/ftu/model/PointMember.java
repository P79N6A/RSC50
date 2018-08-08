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
public class PointMember {
	private int id;
	private String desc;
	private String otherName;
	private String tableName;
	private String dataId;

	public PointMember() {
		super();
	}

	public PointMember(String desc, String tbaleName, String dataId) {
		super();
		this.desc = desc;
		this.tableName = tbaleName;
		this.dataId = dataId;
	}

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

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	@Override
	public String toString() {
		return "PointMember [id=" + id + ", desc=" + desc + ", otherName="
				+ otherName + ", tableName=" + tableName + ", dataId=" + dataId
				+ "]";
	}
}
