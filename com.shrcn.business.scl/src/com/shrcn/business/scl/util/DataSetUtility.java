/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
/**
 * 
 */
package com.shrcn.business.scl.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author zhouhuiming(mailto:zhm.3119@shrcn.com)
 * @version 1.0, 2010-5-13
 */
/**
 * $Log: DataSetUtility.java,v $
 * Revision 1.1  2013/03/29 09:36:32  cchun
 * Add:创建
 *
 * Revision 1.2  2010/12/20 02:38:20  cchun
 * Refactor:格式化
 *
 * Revision 1.1  2010/06/18 09:50:20  cchun
 * Add:添加描述导出、导入功能
 *
 */
public final class DataSetUtility {
	static final String 	NAME_REGEX="([^:]*):?",
							DESC_REGEX=":([^:]*)";
	static final Pattern 	NAME_Pattern=Pattern.compile(NAME_REGEX),
							DESC_Pattern=Pattern.compile(DESC_REGEX);
	
	/**
	 * 获取数据集的名称
	 * @param input : 名称+ ":" +描述
	 * @return 名称
	 */
	public static String getName(String input) {
		if (input == null || "".equals(input.trim()))
			return "";
		Matcher matcher = NAME_Pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return "";
		}
	}

	/**
	 * 获取数据集的描述
	 * @param input : 名称+ ":" +描述
	 * @return 描述
	 */
	public static String getDesc(String input) {
		if (input == null || "".equals(input.trim()))
			return "";
		Matcher matcher = DESC_Pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return "";
		}
	}
}
