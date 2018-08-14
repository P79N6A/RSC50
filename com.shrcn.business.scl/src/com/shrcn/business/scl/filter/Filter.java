/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
/**
 * 
 */
package com.shrcn.business.scl.filter;

/**
 * 
 * @author zhouhuiming(mailto:zhm.3119@shrcn.com)
 * @version 1.0, 2010-6-23
 */
/**
 * $Log: Filter.java,v $
 * Revision 1.1  2013/03/29 09:38:19  cchun
 * Add:创建
 *
 * Revision 1.1  2010/09/14 08:10:55  cchun
 * Refactor:添加filter包
 *
 * Revision 1.1  2010/06/28 02:51:14  cchun
 * Update:添加oid过滤功能
 *
 * @param <T>
 */
public interface Filter<T> {
	void filter(T t);
}
