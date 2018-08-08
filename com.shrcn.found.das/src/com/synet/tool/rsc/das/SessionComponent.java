/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.synet.tool.rsc.das;

import java.sql.Connection;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.w3c.dom.Document;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.tool.found.das.DBManager;
import com.shrcn.tool.found.das.SessionService;

public class SessionComponent implements SessionService {

	protected boolean inited;
	protected Configuration config;
	protected SessionFactory sessionFactory;
	protected String currDbName;
	protected Session session;
	private boolean threadSafe = false;
	protected DBManager dbMgr;
	
	public SessionComponent(DBManager dbMgr) {
		super();
		this.dbMgr = dbMgr;
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
			SCTLogger.error("创建HibernateSessionFactory出错:", e);
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

	@Override
	public Session get() {
		if (session == null || threadSafe) {
			Connection conn = dbMgr.getConnection(currDbName);
			session = sessionFactory.openSession(conn);
			session.setFlushMode(FlushMode.AUTO);
		}
		return session;
	}

	@Override
	public void free() {
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
	public void flush() {
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

