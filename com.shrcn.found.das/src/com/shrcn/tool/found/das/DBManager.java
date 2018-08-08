package com.shrcn.tool.found.das;
/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */


import java.io.InputStream;
import java.sql.Connection;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-10-11
 */
/**
 * $Log: DBManager.java,v $
 * Revision 1.3  2012/11/21 08:17:23  cchun
 * Update:修复同名db判断错误
 *
 * Revision 1.2  2012/10/22 07:26:12  cchun
 * Update:添加分页缓存HB_CACHESIZE
 *
 * Revision 1.1  2012/10/17 08:03:55  cchun
 * Add:数据库访问接口
 *
 */
public interface DBManager {

	public static final int HB_CACHESIZE 	= 50;
	
	/**
	 * 获取连接。
	 * @return
	 */
	public abstract Connection getConnection(String dbName);

	/**
	 * 关闭数据库
	 */
	public abstract void shutdown();

	/**
	 * 执行数据库脚本
	 * @param inputstream
	 * @param connection
	 * @return
	 */
	public abstract int runScript(InputStream inputstream, Connection connection);

}