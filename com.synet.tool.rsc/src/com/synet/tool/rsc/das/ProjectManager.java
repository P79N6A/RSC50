/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.synet.tool.rsc.das;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;

import com.shrcn.found.common.Constants;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.tool.found.das.DBManager;
import com.synet.tool.rsc.RSCConstants;

public class ProjectManager {
	
	private SessionComponent session = null;
	private DBManager dbmgr = null;
	private String hbCfg = "";
	private String sqlCfg = "";
	private static final String dataDir = RSCConstants.RSC_PRJ_NAME;
	
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

	private void setAttr() {
		session = SessionRsc.getInstance();
		dbmgr = RscDbManagerImpl.getInstance();
		hbCfg = RSCConstants.RSC_HB_CFG;
		sqlCfg = RSCConstants.RSC_SQL;
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
		InputStream is = ProjectManager.class.getClassLoader().getResourceAsStream(sqlCfg);
		dbmgr.runScript(is, conn);
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
		return new File(getDataDir() + dbName).exists();
	}
	
	/**
	 * 删除DTU DB
	 * @param rtuname
	 * @return
	 */
	public boolean removeDb(String devName) {
		String path = getDataDir() + devName;
		return FileManipulate.deleteDir(path);
	}
	
	/**
	 * 得到程序根目录
	 * @return
	 */
	private static String getRootDir(){
		return RSCConstants.USR_DIR;
	}
	
	/**
	 * 得到工程目录
	 * @param prjname
	 * @return
	 */
	public static String getDataDir() {
		return getRootDir() + dataDir + File.separator;
	}

	/**
	 * 得到rtu工程路径
	 * @param prjname
	 * @return
	 */
	public static String getProjectDir(String prjname) {
		return getDataDir() + prjname;
	}
	
	/**
	 * 得到工程管理文件路径.
	 * 
	 * @return
	 */
	public static String getProjectFilePath() {
		return getDataDir() + ".project";
	}
	
	/**
	 * 得到工程cid文件导出路径
	 * @return
	 */
	public String getProjectCidPath() {
		return Constants.usrDir + File.separator + "cids" + File.separator + 
			Constants.CURRENT_PRJ_NAME + File.separator;
	}
	
	/**
	 * 得到工程SCD文件路径.
	 * 
	 * @return
	 */
	public String getProjectScdPath() {
		return getDataDir() + Constants.CURRENT_PRJ_NAME + File.separator + RSCConstants.CURR_SCD;
	}
	
	/**
	 * 得到工程SSD文件路径.
	 * 
	 * @return
	 */
	public String getProjectSsdPath() {
		return getDataDir() + Constants.CURRENT_PRJ_NAME + File.separator + RSCConstants.CURR_SSD;
	}
	
	/**
	 * 得到工程SCD文件路径.
	 * 
	 * @return
	 */
	public String getProjectScdTempPath() {
		return getProjectScdPath() + ".bak";
	}
	
	/**
	 * 得到工程SSD文件路径.
	 * 
	 * @return
	 */
	public String getProjectSsdTempPath() {
		return getProjectSsdPath() + ".bak";
	}
	
	/**
	 * 得到IED RSC文件路径。
	 * @param iedName
	 * @return
	 */
	public static String getRscFilePath(String iedName) {
		return Constants.tempDir + File.separator + iedName + ".rsc";
	}
}

