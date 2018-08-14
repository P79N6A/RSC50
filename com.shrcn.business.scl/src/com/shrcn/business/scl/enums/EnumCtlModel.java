/**
 * Copyright (c) 2007, 2016 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.enums;

/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2016-1-14
 */
public enum EnumCtlModel {
	
	status("0", "不可控","status-only"),
	direct_n_scurity("1", "直控","direct-with-normal-security"),
	sbo_n_scurity("2", "选控","sbo-with-normal-security"),
	direct_e_scurity("3", "增强型直控","direct-with-enhanced-security"),
	sbo_e_scurity("4", "增强型选控","sbo-with-enhanced-security");
	
	private String type;
	private String desc;
	private String name;
	
	EnumCtlModel(String type, String desc, String name) {
		this.type = type;
		this.desc = desc;
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static String getDescByName(String name){
		for (EnumCtlModel cm : values()) {
			if (cm.getName().equals(name)) {
				return cm.getDesc();
			}
		}
		return name;
	}
}
