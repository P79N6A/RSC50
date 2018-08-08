/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.valid;

import com.shrcn.found.common.util.StringUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-3-3
 */
/**
 * $Log: DataTypeChecker.java,v $
 * Revision 1.1  2013/03/29 09:37:48  cchun
 * Add:创建
 *
 * Revision 1.7  2013/01/10 03:51:13  cchun
 * Fix Bug:修复短地址校验缺陷
 *
 * Revision 1.6  2011/04/07 09:28:35  cchun
 * Update:添加方法checkPort()
 *
 * Revision 1.5  2011/03/22 08:51:06  cchun
 * Update:清理引用
 *
 * Revision 1.4  2011/03/16 06:42:40  cchun
 * Refactor:修改校验监听器
 *
 * Revision 1.3  2011/02/21 03:31:49  cchun
 * Update:统一数据校验
 *
 * Revision 1.2  2010/09/21 00:56:10  cchun
 * Update:添加数字字串校验
 *
 * Revision 1.1  2010/08/24 01:24:44  cchun
 * Refactor:修改包路径
 *
 * Revision 1.4  2010/08/17 03:09:11  cchun
 * Update:添加数据校验方法
 *
 * Revision 1.3  2010/07/15 05:47:02  cchun
 * Update:添加IP地址校验方法
 *
 * Revision 1.2  2010/03/16 12:17:04  cchun
 * Update: 更新
 *
 * Revision 1.1  2010/03/03 03:43:24  cchun
 * Refactor:将数据校验提取到独立的class中
 *
 */
public class DataTypeChecker {
	
	/**
	 * 判断字符串是否全为0-9的数字
	 * @param value
	 * @return
	 */
	public static boolean checkDigit(String value) {
		return value.matches(EnumDataType.Digit.getRegex());
	}
	
	/**
	 * 判断字符串是否为整数
	 * @param value
	 * @return
	 */
	public static boolean checkInteger(String value) {
		return value.matches(EnumDataType.Int.getRegex());
	}

	/**
	 * 判断字符串是否为数字和小数点
	 * @param digit
	 * @return
	 */
	public static boolean checkFloat(String value) {
		return value.matches(EnumDataType.Float.getRegex());
	}
	
	/**
	 * 判断字符串是否为16进制数字（字母大写）或空格
	 * @param name
	 * @return
	 */
	public static boolean checkHexDigitLetter(String value) {
		return value.matches(EnumDataType.HexDigit.getRegex());
	}

	/**
	 * 校验MAC地址前三个字节
	 * 
	 * @param mac
	 * @return
	 */
	public static boolean checkMacFirst(String mac, String name) {
		String macFirst = "01-0C-CD-";
		if (mac.startsWith(macFirst)) {
			int length = macFirst.length();
			String num4 = mac.substring(length, length + 2);
			if (name.equals("GSE") && num4.equals("01")) {
				return true;
			} else if (name.equals("SMV") && num4.equals("04")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * IP地址校验
	 * @param value
	 * @return
	 */
	public static boolean checkIP(String value) {
		return value.matches(EnumDataType.IP.getRegex());
	}

	/**
	 * 子网掩码校验
	 * @param value
	 * @return
	 */
	public static boolean checkNetMask(String value) {
		return value.matches(EnumDataType.NETMASK.getRegex());
	}
	
	/**
	 * IP/NET校验
	 */
	public static boolean checkIPNet(String value) {
		return value.matches(EnumDataType.IPNET.getRegex());
	}
	
	/**
	 * 端口号校验
	 * @param value
	 * @return
	 */
	public static boolean checkPort(String value) {
		if (StringUtil.isEmpty(value))
			return false;
		if (!checkDigit(value))
			return false;
		int port = Integer.valueOf(value);
		return port > -1 && port < 65536;
	}
	
	/**
	 * 校验用户输入是否满足指定类型要求。
	 * @param dataType
	 * @param msg
	 * @param value
	 * @return 正确返回true，否则false。
	 */
	public static boolean check(EnumDataType dataType, String value) {
		String regex = dataType.getRegex();
		return check(regex, value);
	}
	
	/**
	 * 校验用户输入是否满足指定的正则表达式要求。
	 * @param regex
	 * @param msg
	 * @param value
	 * @return 正确返回true，否则false。
	 */
	public static boolean check(String regex, String value) {
		if (!value.matches(regex)) {
			return false;
		}
		return true;
	}
}
