/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
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
 * @author 李蒙龙(mailto:202137/siyuan.com)
 * @version 1.0, 2017-5-18
 */
public abstract class ToolDBManagerImpl implements DBManager {
	
	protected String prjDbName = "";
	protected String url = "";
	protected String user = "";
	protected String shutUrl = "";
	private static String driver  = "org.apache.derby.jdbc.EmbeddedDriver";
	private static String sqlEncode  = Constants.CHARSET_UTF8;
	private Connection conn;
	
	public ToolDBManagerImpl() {
		super();
		setProDbName();
		url = "jdbc:derby:./"+ prjDbName +"/%s;create=true;user=" + user + ";password=123456";
//		url = "jdbc:derby:D:/develop/eclipse-rcp-indigo-SR2-win32/eclipse/RscData/ppp;create=true;user=" + user + ";password=123456";
		shutUrl = "jdbc:derby:;shutdown=true;user=" + user + ";password=123456";
	}
	
	protected abstract void setProDbName();
	
	private String getDbUrl(String dbName) {
		return String.format(url, dbName);
	}
	
	public Connection getConnection(String dbName) {
		if (StringUtil.isEmpty(dbName))
			return null;
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(getDbUrl(dbName));
		} catch (Exception except) {
			SCTLogger.error("创建数据库连接：", except);
		}
		return conn;
	}

	public void shutdown() {
		try {
			DriverManager.getConnection(shutUrl);
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
//			e.printStackTrace();
		}
	}
	
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
	
	public Connection getConn() {
		return conn;
	}
}

