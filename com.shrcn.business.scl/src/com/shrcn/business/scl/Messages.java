/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2010-1-19
 */
/*
 * 修改历史
 * $Log: Messages.java,v $
 * Revision 1.1  2010/01/19 09:02:35  lj6061
 * add:统一国际化工程
 *
 */
public class Messages {

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle("com.shrcn.business.scl.messages", Locale.getDefault());

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