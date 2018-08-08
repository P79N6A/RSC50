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
public class DtuPointType {
	
	private int id;
	private String name;
	
	private DtuPointType perId;
	
	public DtuPointType() {
		super();
	}
	
	public DtuPointType(String name) {
		super();
		this.name = name;
	}

	public DtuPointType(String name, DtuPointType perId) {
		super();
		this.name = name;
		this.perId = perId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public DtuPointType getPerId() {
		return perId;
	}
	
	public void setPerId(DtuPointType perId) {
		this.perId = perId;
	}
	
}

