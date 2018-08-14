/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model;

import java.io.Serializable;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-6-2
 */
/*
 * 修改历史
 * $Log: IED.java,v $
 * Revision 1.1  2013/03/29 09:36:38  cchun
 * Add:创建
 *
 * Revision 1.6  2011/06/22 02:52:35  cchun
 * Update:增加制造厂商信息
 *
 * Revision 1.5  2011/01/18 09:45:07  cchun
 * Update:去掉对Pin的无效引用
 *
 * Revision 1.4  2011/01/10 09:18:54  cchun
 * Update:为IED添加构造方法
 *
 * Revision 1.3  2010/09/03 02:40:50  cchun
 * Update:增加type属性
 *
 * Revision 1.2  2009/06/23 04:02:54  cchun
 * Refactor:重构绘图模型
 *
 * Revision 1.1  2009/06/17 11:21:35  hqh
 * 添加ied模型
 *
 * Revision 1.1  2009/06/15 08:00:33  hqh
 * 修改图形实现
 *
 */
public class IED implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String desc;
	private String type;
	private String manufacturer;
	private String configVersion;
	private String vtCRC;
	
	/**
	 * 构造方法
	 * @param name
	 */
	public IED(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}
	
	/**
	 * 构造方法
	 * @param name
	 * @param desc
	 * @param type
	 */
	public IED(String name, String desc, String type, String manufacturer, String configVersion) {
		this(name, desc);
		this.type = type;
		this.manufacturer = manufacturer;
		this.configVersion = configVersion;
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

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
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

	public String getVtCRC() {
		return vtCRC;
	}

	public void setVtCRC(String vtCRC) {
		this.vtCRC = vtCRC;
	}
	
}
