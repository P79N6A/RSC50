/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
/**
 * 
 */
package com.shrcn.business.scl.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zhouhuiming(mailto:zhm.3119@shrcn.com)
 * @version 1.0, 2010-5-31
 */
/**
 * $Log: ReceivedInput.java,v $
 * Revision 1.3  2010/12/14 10:23:58  cchun
 * Update:添加泛型类型
 *
 * Revision 1.2  2010/10/18 02:42:33  cchun
 * Update:清理引用
 *
 * Revision 1.1  2010/06/18 09:46:00  cchun
 * Update:设置初始菜单禁用
 *
 */
public class ReceivedInput {
	public static String ied;
	public static String iedName;
	public static String iedType;
	/*
	 * path格式:
	 * LDeviceInst/prefix+lnClass+lnlnst.DO.DA
	 */
	public String path;
	public String desc;
	public String name;
	public List<LDevice> lstLDevice = new ArrayList<LDevice>();
}
