/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.xml.schema;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-4-30
 */
/**
 * $Log: LnClass.java,v $
 * Revision 1.1  2013/03/29 09:38:01  cchun
 * Add:创建
 *
 * Revision 1.3  2011/08/15 02:24:13  cchun
 * Update:添加num属性
 *
 * Revision 1.2  2011/01/25 02:06:02  cchun
 * Add:聂国勇增加，增加同时添加多个逻辑节点功能
 *
 * Revision 1.1  2010/04/30 06:28:39  cchun
 * Add:lnClass直接从schema读取
 *
 */
public class LnClass {
	private String name;
	private String desc;
	private int num = 0;
	private List<LnClass> children = new ArrayList<LnClass>();
	private LnClass parent = null;
	
	public LnClass(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}
	
	public LnClass getParent(){
		return parent;
	}
	
	public void setParent(LnClass p){
		parent = p;
	}
	/**
	 * 添加子元素
	 * @param lnClass
	 */
	public void addChild(LnClass lnClass) {
		children.add(lnClass);
	}
	
	/**
	 * 获取子元素集合
	 * @return
	 */
	public List<LnClass> getChildren() {
		return this.children;
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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void setChildren(List<LnClass> children) {
		this.children = children;
	}
}
