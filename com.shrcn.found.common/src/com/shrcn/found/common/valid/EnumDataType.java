/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.valid;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-2-19
 */
/**
 * $Log: EnumDataType.java,v $
 * Revision 1.4  2013/07/29 06:38:19  cxc
 * Add:添加描述
 *
 * Revision 1.3  2013/01/10 03:51:13  cchun
 * Fix Bug:修复短地址校验缺陷
 *
 * Revision 1.2  2011/11/21 09:42:44  cchun
 * Update:16进制校验允许小写
 *
 * Revision 1.1  2011/02/21 03:31:49  cchun
 * Update:统一数据校验
 *
 */
public enum EnumDataType {
	
	HexDigit("[\\dA-Fa-f]+", "十六进制数"),
	Float("(-?\\d+)(\\.\\d+)?", "浮点数"),
	Digit("\\d+", "数字"),
	Int("(-?\\d+)", "整数"),
	IP("^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])(\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])){3}$", 
			"IP地址"),
	NETMASK("^(254|252|248|240|224|192|128|0)\\.0\\.0\\.0$|^(255\\.(254|252|248|240|224|192|128|0)\\.0\\.0)$|^(255\\.255\\.(254|252|248|240|224|192|128|0)\\.0)$|^(255\\.255\\.255\\.(254|252|248|240|224|192|128|0))$", 
					"子网掩码"),
	IPNET("(0|[1-9][0-9]?|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.(0|[1-9][0-9]?|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.(0|[1-9][0-9]?|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.(0|[1-9][0-9]?|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\/[A-Za-z0-9]+$",
			"IP地址和网口");
	
	private String regex;
	private String desc;
	
	EnumDataType(String regex, String desc) {
		this.regex = regex;
		this.desc = desc;
	}
	
	public String getRegex() {
		return regex;
	}

	public String getDesc() {
		return desc;
	}
	
}
