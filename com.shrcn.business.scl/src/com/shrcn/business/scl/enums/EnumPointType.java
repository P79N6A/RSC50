/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.enums;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-11-28
 */
/**
 * $Log: EnumPointType.java,v $
 * Revision 1.1  2013/03/29 09:36:38  cchun
 * Add:创建
 *
 * Revision 1.3  2011/12/09 02:21:02  cchun
 * Update:添加故障、录波
 *
 * Revision 1.2  2011/12/02 03:42:36  cchun
 * Update:添加信号种类
 *
 * Revision 1.1  2011/11/30 11:12:47  cchun
 * Add:工程点表类型枚举
 *
 */
public enum EnumPointType {
	YX("遥信"), YC("遥测"), SJ("事件"), GJ("告警"), 
	YB("压板"), DZ("定值"), GZ("故障"), LB("录波");
	private String desc;

	EnumPointType(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	
	public static String[] getComboItems() {
		EnumPointType[] types = EnumPointType.values();
		int len = types.length;
		String[] items = new String[len];
		for (int i=0; i<len; i++) {
			items[i] = types[i].getDesc();
		}
		return items;
	}
}
