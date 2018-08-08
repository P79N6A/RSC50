/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.ftu.model;

 /**
 * 
 * @author 李蒙龙(mailto:202137/siyuan.com)
 * @version 1.0, 2017-1-14
 */
public class DeviceDict {

	private int id;
	private String code;
	private String name;
	private String parentCode;
	
	public DeviceDict() {
		super();
	}

	public DeviceDict(int id) {
		super();
		this.id = id;
	}
	
	public DeviceDict(String code, String name, String parentCode) {
		super();
		this.code = code;
		this.name = name;
		this.parentCode = parentCode;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

