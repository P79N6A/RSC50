/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;


/**
 * 
 * @author 普洪涛(mailto:pht@shrcn.com)
 * @version 1.0, 2009-6-23
 */
/*
 * 修改历史
 * $Log: PortReferenceData.java,v $
 * Revision 1.1  2013/03/29 09:36:36  cchun
 * Add:创建
 *
 * Revision 1.3  2011/09/21 09:18:28  cchun
 * Refactor:改变插件
 *
 * Revision 1.1  2011/01/18 09:47:15  cchun
 * Update:修改包名
 *
 * Revision 1.1  2009/06/25 03:06:53  pht
 * 引用端子数据类更改到这个包中。
 *
 * Revision 1.1  2009/06/24 02:19:41  pht
 * 引用端子视图数据
 *
 */
public class PortReferenceData {
	
	private List<Element> listElement = new ArrayList<Element>();
	private String iedName;
	private String iedDesc;
	private String doName;
	private String doDesc;
	
	public List<Element> getListElement() {
		return listElement;
	}
	public void setListElement(List<Element> listElement) {
		this.listElement = listElement;
	}
	public String getIedName() {
		return iedName;
	}
	public void setIedName(String iedName) {
		this.iedName = iedName;
	}
	public String getIedDesc() {
		return iedDesc;
	}
	public void setIedDesc(String iedDesc) {
		this.iedDesc = iedDesc;
	}
	public String getDoName() {
		return doName;
	}
	public void setDoName(String doName) {
		this.doName = doName;
	}
	public String getDoDesc() {
		return doDesc;
	}
	public void setDoDesc(String doDesc) {
		this.doDesc = doDesc;
	}
	
}
