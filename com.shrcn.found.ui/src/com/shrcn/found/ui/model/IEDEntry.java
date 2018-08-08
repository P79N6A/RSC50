/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.model;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-5
 */
/**
 * $Log: IEDEntry.java,v $
 * Revision 1.1  2013/04/06 05:33:38  cchun
 * Add:导航树基础类
 *
 */
public class IEDEntry extends ContainerEntry {

	private String type;
	private String manufacturer;
	private String configVersion;
	
	public IEDEntry(ITreeEntry parent, String name, String desc, String icon) {
		super(parent, name, desc, icon);
	}
	
	public IEDEntry(ITreeEntry parent, String name, String desc, String icon, int index) {
		this(parent, name, desc, icon);
		setIndex(index);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getConfigVersion() {
		return configVersion;
	}

	public void setConfigVersion(String configVersion) {
		this.configVersion = configVersion;
	}
}
