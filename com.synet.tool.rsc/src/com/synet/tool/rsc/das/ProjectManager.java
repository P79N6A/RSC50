/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.synet.tool.rsc.das;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;

import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.tool.found.das.DBManager;
import com.synet.tool.rsc.RSCConstants;

public class ProjectManager {
	
	protected SessionComponent session = null;
	protected DBManager dbmgr = null;
	protected String hbCfg = "";
	protected String sqlCfg = "";
	protected String prjName = "";
	
	private static ProjectManager inst;
	
	public static ProjectManager getInstance() {
		if(inst == null) {
			inst = new ProjectManager();
		}
		return inst;
	}
	
	private ProjectManager() {
		super();
		if(dbmgr != null) {
			closeDB();
		}
		setAttr();
	}

	protected void setAttr() {
		session = SessionRsc.getInstance();
		dbmgr = RscDbManagerImpl.getInstance();
		hbCfg = RSCConstants.RSC_HB_CFG;
		sqlCfg = RSCConstants.RSC_SQL;
		prjName = RSCConstants.RSC_PRJ_NAME;
	}
	
	/**
	 * 关闭连接
	 */
	public void closeDB() {
		dbmgr.shutdown();
	}
	
	/**
	 * 创建工程
	 * @param devName
	 */
	public void initDb(String dbName) {
		session.reset(dbName);
		Connection conn = dbmgr.getConnection(dbName);
		if (!exists(dbName)) {
			InputStream is = ProjectManager.class.getClassLoader().getResourceAsStream(sqlCfg);
			dbmgr.runScript(is, conn);
		}
		session.init(hbCfg);
	}
	
	/**
	 * 打开 DB
	 * @param devName
	 */
	public boolean openDb(String devName) {
		if (!exists(devName))
			return false;
		dbmgr.getConnection(devName);
		session.reset(devName);
		session.init(hbCfg);
		return true;
	}
	
	/**
	 * 判断是否存在
	 * @param rtuname
	 * @return
	 */
	public boolean exists(String dbName) {
		return new File(getProjectDir() + dbName).exists();
	}
	
	/**
	 * 删除DTU DB
	 * @param rtuname
	 * @return
	 */
	public boolean removeDb(String devName) {
		dbmgr.shutdown();
		String path = getProjectDir() + devName;
		return FileManipulate.deleteDir(path);
	}
	
	/**
	 * 得到程序根目录
	 * @return
	 */
	private String getRootDir(){
		return RSCConstants.USR_DIR + File.separator;
	}
	
	/**
	 * 得到工程目录
	 * @param prjname
	 * @return
	 */
	public String getProjectDir() {
		return getRootDir() + prjName + File.separator;
	}

}

