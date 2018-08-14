/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.dialog;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-1-18
 */
/**
 * $Log: Messages.java,v $
 * Revision 1.1  2013/06/28 08:45:25  cchun
 * Update:添加模型检查和虚端子关联查看
 *
 * Revision 1.1  2010/03/02 07:48:40  cchun
 * Add:添加重构代码
 *
 * Revision 1.1  2010/01/19 09:37:14  gj
 * Update:提交国际化支持
 *
 */
public class Messages {
	private static final String BUNDLE_NAME = "com.shrcn.business.scl.dialog.messages"; //$NON-NLS-1$

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
