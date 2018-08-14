/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-1-21
 */
/**
 * $Log: Messages.java,v $
 * Revision 1.1  2010/03/02 07:49:39  cchun
 * Add:添加重构代码
 *
 * Revision 1.1  2010/01/21 08:47:55  gj
 * Update:完成UI插件的国际化字符串资源提取
 *
 */
public class Messages {
	private static final String BUNDLE_NAME = "com.shrcn.business.scl.ui.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
