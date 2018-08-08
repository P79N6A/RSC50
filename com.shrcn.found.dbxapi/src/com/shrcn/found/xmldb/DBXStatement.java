/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb;

import java.io.InputStream;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-1-11
 */
/**
 * $Log: DBXStatement.java,v $
 * Revision 1.1  2013/03/29 09:37:03  cchun
 * Add:创建
 *
 * Revision 1.1  2012/01/13 08:39:50  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 */
public interface DBXStatement {
	
	/**
	 * 执行xquery文件。
	 * @param xqIS
	 * @return
	 */
	public boolean execute(InputStream xqIS);
	
	/**
	 * 执行xquery语句字符串。
	 * @param xquery
	 * @return
	 */
	public boolean execute(String xquery);
	
	/**
	 * 获取查询结果集。
	 * @return
	 */
	public DBXSerializedResult getSerializedResult();
}
