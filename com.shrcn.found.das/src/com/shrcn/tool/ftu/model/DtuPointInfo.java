/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.ftu.model;

 /**
 * 
 * @author 李蒙龙(mailto:202137/siyuan.com)
 * @version 1.0, 2017-4-12
 */
public class DtuPointInfo {
	
	private int id;
	private String addr;
	private DtuPointType type;
	private String description;
	
	public DtuPointInfo() {
		super();
	}

	public DtuPointInfo(String addr, DtuPointType type, String description) {
		super();
		this.addr = addr;
		this.type = type;
		this.description = description;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}



	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public DtuPointType getType() {
		return type;
	}


	public void setType(DtuPointType type) {
		this.type = type;
	}
	
}

