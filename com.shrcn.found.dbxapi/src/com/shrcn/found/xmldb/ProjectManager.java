/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb;

import java.util.List;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.xmldb.bx.ProjectManagerImpl;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-1-11
 */
/**
 * $Log: ProjectManager.java,v $
 * Revision 1.2  2013/04/07 12:24:56  cchun
 * Refactor:清理引用
 *
 * Revision 1.1  2013/03/29 09:37:03  cchun
 * Add:创建
 *
 * Revision 1.3  2012/10/17 08:00:42  cchun
 * Refactor:改变静态变量初始化时机，便于测试
 *
 * Revision 1.2  2012/04/19 10:27:12  cchun
 * fixBug:释放数据库
 *
 * Revision 1.1  2012/01/13 08:39:51  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 */
public class ProjectManager {
	
	public static PrjMgrBroker prjMgr = new ProjectManagerImpl();
	
	private ProjectManager() {}
	
	/**
	 * 判断工程是否存在
	 * @param prjName URL格式工程名。
	 * @return
	 */
	public static void initPrjectDB(String prjName) {
		prjMgr.initPrjectDB(prjName);
	}

	/**
	 * 判断工程是否存在
	 * @param prjName URL格式工程名。
	 * @return
	 */
	public static void clearPrjectDB(String prjName) {
		closePrjectDB(prjName);
		prjMgr.clearPrjectDB(prjName);
	}
	
	public static void closePrjectDB(String prjName) {
		XMLDBHelper.close();
		XMLDBHelperWithDoc.close();
		try {
			Collection col = XMLDBAdmin.getCollection(prjName);
			if (col != null)
				col.close();
		} catch (XMLDBException e) {
			SCTLogger.error("关闭Collection出错：", e);
		}
	}

	/**
	 * 判断工程是否存在
	 * @param prjName URL格式工程名。
	 * @return
	 */
	public static boolean existProject(String prjName) {
		return Constants.XQUERY ? prjMgr.existProject(prjName) : false;
	}

	/**
	 * 返回所有工程名。
	 * @return 数据库工程名（普通字符串格式）集合。
	 */
	public static List<String> getHistoryItems() {
		return prjMgr.getHistoryItems();
	}
}
