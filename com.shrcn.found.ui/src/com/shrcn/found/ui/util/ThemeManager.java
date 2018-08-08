/**
 * Copyright (c) 2007-2014 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on substation automation.
 */
package com.shrcn.found.ui.util;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2014-5-4
 */
/**
 * $Log$
 */
public class ThemeManager {

//	private static ColorCache colorCache = ColorCache.getInstance();
	
	private static ThemeManager inst = new ThemeManager();
	
	public static ThemeManager getInstance() {
		if (inst == null) {
			inst = new ThemeManager();
		}
		return inst;
	}
	
	private ThemeManager() {}
	
	
}
