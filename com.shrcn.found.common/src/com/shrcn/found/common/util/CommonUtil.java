/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Visual Device Develop System.
 */
package com.shrcn.found.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 黄钦辉
 * @version 1.0, 2008-8-22
 */
public class CommonUtil {

	/**
	 * 格式化日期字符串
	 * 
	 * @param date
	 *            给定的日期
	 * @return
	 */
	public static String getFormattedDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
		return format.format(date);
	}

	/**
	 * 将字符串转换成整数.
	 * 
	 * @param v
	 *            the v
	 * 
	 * @return the integer value
	 */
	public static int getIntegerValue(String v) {
		try {
			Integer integer = new Integer(v);
			return integer.intValue();
		} catch (NumberFormatException e) {
		}

		return 0;
	}

	public static int valueOfInt(String str, int radix){
		int val = 0;
		try {
			val = Integer.valueOf(str.trim(), radix);
		} catch (NumberFormatException e) {
		}
		return val;
	}

	/**
	 * 将字符串转换成浮点数.
	 * 
	 * @param v
	 *            the v
	 * 
	 * @return the double value
	 */
	public static double getDoubleValue(String v) {
		try {
			Double val = new Double(v);
			return val.doubleValue();
		} catch (NumberFormatException e) {
		}

		return 0;
	}
	
	/**
	 * 字符串转换成Long型数.
	 * 
	 * @param v
	 *            the v
	 * 
	 * @return the long value
	 */
	public static long getLongValue(String v) {
		long parseLong = -1;
		try {
			parseLong = Long.parseLong(v);
		} catch (NumberFormatException e) {
		}

		return parseLong;
	}

	/**
	 * 字符串转换成布尔值.
	 * 
	 * @param v
	 *            the v
	 * 
	 * @return the boolean value
	 */
	public static boolean getBooleanValue(String v) {
		if (v == null) {
			return Boolean.FALSE;
		}

		return Boolean.valueOf(v);
	}

	/**
	 * 16进制转换到10进制
	 */
	public static int convert16To10(String value) {
		return Integer.parseInt(value, 16);
	}

	/**
	 * 10进制转换到16进制
	 */
	public static String convert10To16(int value) {
		return Integer.toHexString(value);
	}

	/**
	 * 格式化字符串 
	 * 
	 * @param value
	 *            转换值
	 * @param 固定转换后的位数
	 * @param type "c" 字符
	 * 				"d" 整数 "x" 16进制数
	 */
	public static String formatFillZero(int value, int size, String type) {
		return String.format("%1$0" + size + type, value); //$NON-NLS-1$
	}
	
	/**
	 * 请使用ObjectUtil.clone()
	 * @deprecated
	 * @param original
	 * @return
	 */
	public static Object clone(Object original) {
		Object clone = null;
		try {
			// Increased buffer size to speed up writing
			ByteArrayOutputStream bos = new ByteArrayOutputStream(5120);
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(original);
			out.flush();
			out.close();

			ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(bos.toByteArray()));
			clone = in.readObject();

			in.close();
			bos.close();

			return clone;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}

		return null;
	}
}
