/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.enums;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-12-29
 */
/**
 * $Log: EnumCommType.java,v $
 * Revision 1.1  2013/03/29 09:36:39  cchun
 * Add:创建
 *
 * Revision 1.2  2011/11/16 09:06:55  cchun
 * Update:增加方法getType()
 *
 * Revision 1.1  2010/12/29 06:43:52  cchun
 * Add:通信枚举类
 *
 */
public enum EnumCommType {
	MMS("mms"), GSE("goose"), SMV("smv");
	
	private String cfgFile;
	
	EnumCommType(String cfgFile) {
		this.cfgFile = cfgFile;
	}

	public String getCfgFile() {
		return cfgFile;
	}
	
	public static EnumCommType getType(String cfgName) {
		if ("Address".equals(cfgName))
			return MMS;
		else if ("GSE".equals(cfgName))
			return GSE;
		else if ("SMV".equals(cfgName))
			return SMV;
		else
			return null;
	}
}
