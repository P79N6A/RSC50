/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.common.util;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-23
 */
/**
 * $Log: TimeCounter.java,v $
 * Revision 1.1  2013/04/23 13:50:27  cchun
 * Add:计时器
 *
 */
public class TimeCounter {

	private static long begin = 0l;
	
	public static void begin() {
		begin = System.currentTimeMillis();
	}
	
	public static String end(String title) {
		long time = System.currentTimeMillis() - begin;
		String msg = title + getTimeStr(time);
		System.out.println(msg);
		return msg;
	}
	
	public static String getTimeStr(long total) {
		long m = total / (60*1000);
		long s = (total / 1000) % 60;
		long mm = total % 1000;
		return "耗时：" + m + "分钟" + s + "秒" + mm + "毫秒";
	}
	
	public static String end(String title, boolean reset) {
		String msg = end(title);
		if (reset)
			begin();
		return msg;
	}
}
