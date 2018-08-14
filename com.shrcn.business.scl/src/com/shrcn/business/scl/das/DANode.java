/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-11-11
 */
/**
 * $Log: DANode.java,v $
 * Revision 1.1  2013/03/29 09:36:21  cchun
 * Add:创建
 *
 * Revision 1.1  2009/11/13 07:18:10  cchun
 * Update:完善关联表格功能
 *
 */
public class DANode {
	private String daName;
	private String daType;
	private String bType;
	private String fc;
	private DANode parent;
	private List<DANode> children = new ArrayList<DANode>();
	
	public DANode(String daName, String daType, String bType) {
		this.daName = daName;
		this.daType = daType;
		this.bType = bType;
	}
	
	public DANode(String daName, String daType, String bType, String fc) {
		this(daName, daType, bType);
		this.fc = fc;
	}
	
	public List<DANode> getChildren() {
		return children;
	}
	
	public void addChild(DANode child) {
		children.add(child);
	}
	
	public String getDaName() {
		return daName;
	}
	public void setDaName(String daName) {
		this.daName = daName;
	}
	public String getDaType() {
		return daType;
	}
	public void setDaType(String daType) {
		this.daType = daType;
	}
	public DANode getParent() {
		return parent;
	}
	public void setParent(DANode parent) {
		this.parent = parent;
	}

	public String getBType() {
		return bType;
	}

	public void setBType(String type) {
		bType = type;
	}

	public String getFc() {
		return fc;
	}

	public void setFc(String fc) {
		this.fc = fc;
	}
}
