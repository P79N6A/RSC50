/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.found.das.impl;

import java.sql.Connection;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.w3c.dom.Document;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.tool.found.das.DBManager;
import com.shrcn.tool.found.das.SessionService;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-10-11
 */
/**
 * $Log: SessionComponent.java,v $
 * Revision 1.9  2013/06/06 11:04:49  cchun
 * Fix Bug:修复批量导入IED时内存溢出的bug
 *
 * Revision 1.8  2013/01/15 04:01:32  cchun
 * Update:恢复并发处理
 *
 * Revision 1.6  2012/11/21 08:17:51  cchun
 * Update:修复同名db判断错误，添加初始化标记
 *
 * Revision 1.5  2012/11/19 07:19:15  cchun
 * Update:调整clear(),flush()顺序
 *
 * Revision 1.4  2012/11/14 07:38:52  cchun
 * Update:给session添加同步
 *
 * Revision 1.3  2012/11/02 06:50:13  cchun
 * Update:增加flush()
 *
 * Revision 1.2  2012/10/25 11:07:27  cchun
 * Update:清理引用
 *
 * Revision 1.1  2012/10/17 08:04:08  cchun
 * Add:数据库访问实现
 *
 */
public class SessionComponent implements SessionService {
	
	protected SessionFactory sessionFactory;
	protected Session session;
	protected DBManager dbMgr = DBManagerImpl.getInstance();
	protected Configuration config;
	protected String currDbName;
	protected static SessionComponent inst;
	protected boolean inited;
	
	private boolean threadSafe = false;
	
	public static SessionComponent getInstance() {
		if (inst == null)
			inst = new SessionComponent();
		
		BeanDaoImpl.getInstance().setService(inst);
		HqlDaoImpl.getInstance().setService(inst);
		
		return inst;
	}
	
	@Override
	public void init(String configURL) {
		try {
			if (!inited) {
				this.config = new Configuration().configure(configURL);
				this.sessionFactory = config.buildSessionFactory();
				inited = true;
			}
		} catch (Exception e) {
			SCTLogger.error("创建Hibernate SessionFactory出错：", e);
		}
	}
	
	@Override
	public void init(Document document) {
		try {
			if (!inited) {
				this.config = new Configuration().configure(document);
				this.sessionFactory = config.buildSessionFactory();
				inited = true;
			}
		} catch (Exception e) {
			SCTLogger.error("创建Hibernate SessionFactory出错：", e);
		}
	}
	
	@Override
	public void reset(String dbName) {
		free();
		this.currDbName = dbName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.org.osgi.module.hibernate.session.SessionService#get()
	 */
	@Override
	public synchronized Session get() {
		if (session == null || threadSafe) {
			Connection conn = dbMgr.getConnection(currDbName);
			session = sessionFactory.openSession(conn);
			session.setFlushMode(FlushMode.AUTO);
		}
		return session;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.org.osgi.module.hibernate.session.SessionService#free()
	 */
	@Override
	public synchronized void free() {
		if (session != null) {
			session.close();
			session = null;
		}
	}

	@Override
	public Configuration getConfiguration() {
		return config;
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public synchronized void flush() {
		if (session != null) {
			session.flush();
			session.clear();
		}
		if (threadSafe)
			free();
	}

	public void setThreadSafe(boolean threadSafe) {
		this.threadSafe = threadSafe;
	}	
}
