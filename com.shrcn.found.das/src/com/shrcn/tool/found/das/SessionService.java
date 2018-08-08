package com.shrcn.tool.found.das;
/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.w3c.dom.Document;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-10-11
 */
/**
 * $Log: SessionService.java,v $
 * Revision 1.2  2012/11/02 06:48:49  cchun
 * Update:增加flush()
 *
 * Revision 1.1  2012/10/17 08:03:55  cchun
 * Add:数据库访问接口
 *
 */
public interface SessionService {
	
	/**
	 * 初始化
	 * @param configURL
	 */
	public void init(String configURL);
	
	/**
	 * 初始化
	 * @param document
	 */
	public void init(Document document);

	/**
	 * 更新数据库连接。
	 * @param dbName
	 */
	public void reset(String dbName);
	
	/**
	 * 获取Session
	 * 
	 * @return Session
	 * @throws Exception
	 */
	public Session get();
	
	/**
	 * 释放Session
	 * 
	 * @throws Exception
	 */
	public void free();
	
	/**
	 * 获取hibernate配置信息
	 * 
	 * @return
	 */
	public Configuration getConfiguration();
	
	public SessionFactory getSessionFactory();

	public void flush();
}
