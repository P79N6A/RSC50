/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.found.das.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.derby.tools.ij;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.tool.found.das.DBManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-10-11
 */
/**
 * $Log: DBManagerImpl.java,v $
 * Revision 1.4  2013/08/05 09:07:35  scy
 * Update:修改工程名常量
 *
 * Revision 1.3  2012/11/23 09:47:17  cchun
 * Fix Bug:修复同名rtu，不同工程不刷新连接的bug
 *
 * Revision 1.2  2012/11/21 08:17:23  cchun
 * Update:修复同名db判断错误
 *
 * Revision 1.1  2012/10/17 08:04:08  cchun
 * Add:数据库访问实现
 *
 */
public class DBManagerImpl implements DBManager {
	
	public static boolean isDBNet = false;
	private static String dbURLEmbed = "jdbc:derby:./rtudata/%s/%s;create=true;user=app;password=123";
	private static String shutURLEmbed = "jdbc:derby:;shutdown=true;user=app;password=123";
	private static String dbURLNet = "jdbc:derby://localhost:1527/rtudata/%s/%s;create=true;user=app;password=123";
	private static String shutURLNet = "jdbc:derby://localhost:1527/;shutdown=true;user=app;password=123";
	private static String driver  = "org.apache.derby.jdbc.EmbeddedDriver";
	private static String sqlEncode  = Constants.CHARSET_UTF8;
	
	private String currDbName;
	private Connection conn;
	private static DBManager inst;
	
	private DBManagerImpl() {
	}
	
	private String getDbUrl(String dbName) {
		String url = isDBNet ? dbURLNet : dbURLEmbed;
		return String.format(url, Constants.DEFAULT_PRJ_NAME, dbName);
	}
	
	public static DBManager getInstance() {
		if (inst == null)
			inst = new DBManagerImpl();
		return inst;
	}
	
	/**
	 * 创建连接
	 */
	private void createConnection(String dbName) {
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(getDbUrl(dbName));
		} catch (Exception except) {
			SCTLogger.error("创建数据库连接：", except);
		}
		this.currDbName = Constants.DEFAULT_PRJ_NAME + "@" + dbName;
	}
	
	/**
	 * 关闭连接
	 */
	private void close() {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException sqlExcept) {
			SCTLogger.error("关闭数据库连接：", sqlExcept);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.tool.found.das.IDBManager#getConnection(java.lang.String)
	 */
	public Connection getConnection(String dbName) {
		if (StringUtil.isEmpty(dbName))
			return null;
		if (!(Constants.DEFAULT_PRJ_NAME + "@" + dbName).equals(currDbName)) {
			createConnection(dbName);
		}
		return conn;
	}

	/* (non-Javadoc)
	 * @see com.shrcn.tool.found.das.IDBManager#shutdown(java.lang.String)
	 */
	public void shutdown() {
		try {
			currDbName = null;
			String shuturl = isDBNet ? shutURLNet : shutURLEmbed;
			DriverManager.getConnection(shuturl);
			close();
		} catch (SQLException sqlExcept) {
			SCTLogger.error("关闭数据库：", sqlExcept);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.shrcn.tool.found.das.IDBManager#runScript(java.io.InputStream, java.sql.Connection)
	 */
	public int runScript(InputStream inputstream, Connection connection) {
		try {
			int result = ij.runScript(connection, inputstream, sqlEncode,
					System.out, sqlEncode);
			return result;
		} catch (UnsupportedEncodingException e) {
			SCTLogger.error("SQL文件编码必须为UTF-8：", e);
			return -1;
		} finally {
			try {
				if (inputstream != null)
					inputstream.close();
			} catch (IOException e) {
				SCTLogger.error("文件IO异常：", e);
			}
		}
	}
}
