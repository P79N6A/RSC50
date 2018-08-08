/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.common.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.ArrayUtils;

/**
 * 
 * @author 吴小兵(mailto:wxb.14417@shrcn.com)
 * @version 1.0, Jun 17, 2016
 */
/**
 * $Log$
 */
public class CharSetUtil {
	public static String getGbkString(byte[] bs){
		String rel = "";
		byte[] temp = new byte[0];
		for (int i = 0; i < bs.length; i++) {
			byte b = bs[i];
			if (b==0){
				break;
			} else {
				temp = ArrayUtils.add(temp, b);
			}
		}
		
		try {
			rel = new String(temp,"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return rel;
	}
	public static String getUtfString(byte[] bs){
		String rel = "";
		byte[] temp = new byte[0];
		for (int i = 0; i < bs.length; i++) {
			byte b = bs[i];
			if (b==0){
				break;
			} else {
				temp = ArrayUtils.add(temp, b);
			}
		}
		
		try {
			rel = new String(temp,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return rel;
	}
}
