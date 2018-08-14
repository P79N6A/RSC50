/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.model;

import org.dom4j.Element;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-8-31
 */
/**
 * $Log: EquipmentInfo.java,v $
 * Revision 1.1  2013/03/29 09:36:39  cchun
 * Add:创建
 *
 * Revision 1.1  2011/09/02 07:07:09  cchun
 * Add:设备配置Bean类
 *
 */
public class EquipmentInfo {

	private String category;
	private String type;
	private String mtype;
	private String desc;
	private String lnode;
	private String terminal;

	public EquipmentInfo() {}
	
	public EquipmentInfo(String category, String type, String mtype, String desc,
			String lnode, String terminal) {
		this.category = category;
		this.type = type;
		this.mtype = mtype;
		this.desc = desc;
		this.lnode = lnode;
		this.terminal = terminal;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMtype() {
		return mtype;
	}

	public void setMtype(String mtype) {
		this.mtype = mtype;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getLnode() {
		return lnode;
	}

	public void setLnode(String lnode) {
		this.lnode = lnode;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	
	public static EquipmentInfo getEqpInfo(Element ele) {
		String category = ele.getParent().attributeValue("type", "");
		return new EquipmentInfo(
				category,
				ele.attributeValue("type", ""),
				ele.attributeValue("mtype", ""),
				ele.attributeValue("desc", ""),
				ele.attributeValue("lnode", ""),
				ele.attributeValue("terminal", "")
		);
	}
}
