/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model;

import org.dom4j.Element;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-5-7
 */
/*
 * 修改历史
 * $Log: OutSingalFCDA.java,v $
 * Revision 1.1  2013/03/29 09:36:40  cchun
 * Add:创建
 *
 * Revision 1.4  2009/05/25 08:23:38  cchun
 * 添加采样值关联拖拽
 *
 * Revision 1.3  2009/05/18 05:54:09  cchun
 * Update:添加属性默认值
 *
 * Revision 1.2  2009/05/12 06:06:29  cchun
 * Update:增加获取ldInst属性方法
 *
 * Revision 1.1  2009/05/08 12:07:17  cchun
 * Update:完善外部、内部信号视图
 *
 */
public class OutSingalFCDA {
	private String desc;
	private Element fcda;
	
	public OutSingalFCDA(Element fcda) {
		this.fcda = fcda;
	}
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getPrefix() {
		return fcda.attributeValue("prefix");
	}
	
	public String getLnClass() {
		return fcda.attributeValue("lnClass");
	}
	
	public String getLnInst() {
		return fcda.attributeValue("lnInst");
	}
	
	public String getDoName() {
		return fcda.attributeValue("doName");
	}
	
	public String getDaName() {
		return fcda.attributeValue("daName");
	}
	
	public String getFC() {
		return fcda.attributeValue("fc");
	}
	
	public String getLdInst() {
		return fcda.attributeValue("ldInst");
	}
	
	public Element getFcda() {
		return fcda;
	}
	public void setFcda(Element fcda) {
		this.fcda = fcda;
	}
}
