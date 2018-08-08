/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.ftu.model;


 /**
 * 
 * @author 李蒙龙(mailto:202137/siyuan.com)
 * @version 1.0, 2016-12-29
 */
public class TableInfo {
	private int id;
	private String tableName;
	private String tableType;
	private String description;
	private String defaultRecordNum;
	private String maxRecordNum;
	private String minRecordNum;
//	private List<Map<String,String>> fields;
	public TableInfo() {
	}
	public TableInfo(int id) {
		super();
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public TableInfo(String tableName, String tableType) {
		super();
		this.tableName = tableName;
		this.tableType = tableType;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableType() {
		return tableType;
	}
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDefaultRecordNum() {
		return defaultRecordNum;
	}
	public void setDefaultRecordNum(String defaultRecordNum) {
		this.defaultRecordNum = defaultRecordNum;
	}
	public String getMaxRecordNum() {
		return maxRecordNum;
	}
	public void setMaxRecordNum(String maxRecordNum) {
		this.maxRecordNum = maxRecordNum;
	}
	public String getMinRecordNum() {
		return minRecordNum;
	}
	public void setMinRecordNum(String minRecordNum) {
		this.minRecordNum = minRecordNum;
	}
}

