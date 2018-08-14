/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.das;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-8-4
 */
/**
 * $Log: IEDInfo.java,v $
 * Revision 1.1  2013/03/29 09:36:23  cchun
 * Add:创建
 *
 * Revision 1.1  2011/08/04 05:21:12  cchun
 * Add:IED基本信息类
 *
 */
public class IEDInfo {
	private String name;
	private String desc;
	private String type;
	private String version;
	private String manufacturer;
	
	public IEDInfo(String name, String desc, 
			String type, String version, String manufacturer) {
		this.name = name;
		this.desc = desc;
		this.type = type;
		this.version = version;
		this.manufacturer = manufacturer;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
}
