/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
/**
 * 
 */
package com.shrcn.business.scl.ui;

/**
 * 
 * @author zhouhuiming(mailto:zhm.3119@shrcn.com)
 * @version 1.0, 2010-8-25
 */
/**
 * $Log: Outputer.java,v $
 * Revision 1.2  2013/06/28 08:55:03  cchun
 * Refactor:重构
 *
 * Revision 1.1  2013/03/29 09:36:45  cchun
 * Add:创建
 *
 * Revision 1.1  2011/09/15 08:40:27  cchun
 * Refactor:修改归属包
 *
 * Revision 1.1  2011/08/01 08:21:38  cchun
 * Refactor:修改接口名称
 *
 * Revision 1.2  2010/12/23 10:10:05  cchun
 * Update:类改接口
 *
 * Revision 1.1  2010/09/03 03:35:33  cchun
 * Refactor:使用公共方法
 *
 */
public interface Outputer {

	public boolean isRun();
	
	public void setRun(boolean isRun);
	
	public void textAppend(String str);
	
	public void labelPrint(String str);
	
	public void moveProgress(int i);
	
	public void setProgressMaxCount(int i);
	
	public void setProgressVisual(boolean state);
}
