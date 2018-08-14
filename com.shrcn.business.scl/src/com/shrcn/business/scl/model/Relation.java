/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.model;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-11-12
 */
/**
 * $Log: Relation.java,v $
 * Revision 1.1  2013/03/29 09:36:36  cchun
 * Add:创建
 *
 * Revision 1.1  2009/11/13 07:18:13  cchun
 * Update:完善关联表格功能
 *
 */
public class Relation {
	private boolean isGroup = false;
	private String groupName;
	public boolean isGroup() {
		return isGroup;
	}
	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
