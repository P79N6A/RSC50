/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.enums;

import com.shrcn.business.scl.common.DefaultInfo;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-7-10
 */
/**
 * $Log: EnumEqpCategory.java,v $
 * Revision 1.1  2013/03/29 09:36:40  cchun
 * Add:创建
 *
 * Revision 1.4  2011/09/02 07:05:33  cchun
 * Update:修改getCategories()和contains()逻辑
 *
 * Revision 1.3  2011/07/20 05:56:59  cchun
 * Update:增加contains()
 *
 * Revision 1.2  2011/07/13 08:35:05  cchun
 * Update:增加getCategories()
 *
 * Revision 1.1  2011/07/11 08:52:56  cchun
 * Add:一次设备定义及其配置文件
 *
 */
public enum EnumEqpCategory {
	
	PowerTransformer(DefaultInfo.SUBS_POWER),
	ConductingEquipment(DefaultInfo.SUBS_CONDUCT),
	GeneralEquipment(DefaultInfo.SUBS_GENERAL);
	
	private int priority;
	
	EnumEqpCategory(int priority) {
		this.priority = priority;
	}
	
	public int getPriority() {
		return this.priority;
	}
	
	public static String[] getCategories() {
		EnumEqpCategory[] catories = EnumEqpCategory.values();
		int length = catories.length;
		String[] values = new String[length];
		for (int i=0; i<length; i++) {
			values[i] = catories[i].name();
		}
		return values;
	}
	
	public static boolean contains(String cate) {
		String[] cates = getCategories();
		for (String c : cates) {
			if (c.equals(cate))
				return true;
		}
		return false;
	}
}
