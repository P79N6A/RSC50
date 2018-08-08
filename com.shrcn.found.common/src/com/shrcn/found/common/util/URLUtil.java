/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.common.util;

import java.io.UnsupportedEncodingException;

import com.shrcn.found.common.Constants;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-8-5
 */

public class URLUtil {
	
	/**
	 * 将字符串转成UTF-8 URL格式 
	 * @param str
	 * @return
	 */
	public static String string2URL(String str) {
		if(str != null) {
			String charSet = Constants.CHARSET_UTF8;
			String unicode = null;
			try {
				unicode = java.net.URLEncoder.encode(str, charSet);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			return unicode;
		}
		return null;
	}

	/**
	 * 将UTF-8 URL转成字符串
	 * @param url
	 * @return
	 */
	public static String url2String(String url){
		if (url != null) {
			String charSet = Constants.CHARSET_UTF8;
			String origin = null;
			try {
				origin = java.net.URLDecoder.decode(url, charSet);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			return origin;
		}
		return null;
	}
}
