/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb;

import java.util.List;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-1-11
 */
/**
 * $Log: PrjMgrBroker.java,v $
 * Revision 1.1  2013/03/29 09:37:02  cchun
 * Add:创建
 *
 * Revision 1.1  2012/01/13 08:39:51  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 */
public interface PrjMgrBroker {
	public void initPrjectDB(String prjName);

	public void clearPrjectDB(String prjName);

	public boolean existProject(String prjName);

	public List<String> getHistoryItems();
}
