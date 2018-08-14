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
 * @version 1.0, 2010-6-8
 */
/**
 * $Log: LDevice.java,v $
 * Revision 1.2  2011/11/25 07:10:12  cchun
 * Refactor:整理代码
 *
 * Revision 1.1  2010/06/18 09:48:11  cchun
 * Add:导入、导出表格模型对象类
 *
 */
public class LDevice {
	public String inst;
	public String apName;
	public List<DataSet> lstDataSet = new ArrayList<DataSet>();
	public List<LNode> lstLNode = new ArrayList<LNode>();
}
