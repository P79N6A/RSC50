/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.common.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-24
 */
/**
 * $Log: ClassUtil.java,v $
 * Revision 1.1  2013/04/24 12:24:13  cchun
 * Add:类查看
 *
 */
public class ClassUtil {

	/**
	 * 输出class方法
	 * 
	 * @param clazz
	 */
	public static void printMethods(Class<?> clazz) {
		Method[] ms = clazz.getMethods();
		for (Method m : ms) {
			if (Modifier.isPublic(m.getModifiers()))
				System.out.println(m.getName());
		}
	}
}
