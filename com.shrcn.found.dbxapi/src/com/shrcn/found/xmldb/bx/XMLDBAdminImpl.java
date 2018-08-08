/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.bx;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.basex.api.xmldb.BXCollection;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.xmldb.DBAdminBroker;
import com.shrcn.found.xmldb.DBXConnection;
import com.shrcn.found.xmldb.DBXTransaction;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-1-11
 */
/**
 * $Log: XMLDBAdminImpl.java,v $
 * Revision 1.1  2013/03/29 09:38:31  cchun
 * Add:创建
 *
 * Revision 1.3  2012/05/18 07:08:28  cchun
 * Update:添加Collection关闭逻辑
 *
 * Revision 1.2  2012/01/31 01:38:12  cchun
 * Refactor:将getCollection()改成私有方法
 *
 * Revision 1.1  2012/01/13 08:39:48  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 */
public class XMLDBAdminImpl implements DBAdminBroker {
	
	private static final String DRIVER = "org.basex.api.xmldb.BXDatabase"; 
    private static Collection col = null;
    private static Database database = null;

 	/**
	 * 注册数据库
	 */
    public boolean startDB() {
		try {
			if (database != null) {
				return true;
			}
			Class<?> clazz = Class.forName(DRIVER); //$NON-NLS-1$
			database = (Database) (clazz.newInstance());
			DatabaseManager.registerDatabase(database);
		} catch (Exception e) {
			SCTLogger.error(e.getMessage());
			return false;
		}
	    return true;
	}
   
	/**
	 * 建立Collection
	 */
   public Collection getCollection() {
	   try {
			if(null != col) {
				if (col.isOpen()) {
					return col;
				} else {
					col.close();
					col = null;
				}
			}
		} catch (XMLDBException e) {
			throw new RuntimeException(e);
		}
		col = getCollection(Constants.DEFAULT_PRJECT_NAME);
		return col;
	}
   
	/**
	 * 建立Collection
	 */
   public Collection getCollection(String projectName) {
	   if (database == null) {
		   startDB();
	   }
	   try {
			if (projectName.equals(Constants.DEFAULT_PRJECT_NAME) 
					   && col != null) {
				if (col.isOpen()) {
				   return col;
				} else {
					col.close();
					col = null;
				}
			 }
		} catch (XMLDBException e2) {
			SCTLogger.error(e2.getMessage());
		}
		Collection col = null;
		try {
			// 打开已经存在的
			col = new BXCollection(projectName, true, database);
		} catch (Exception e) {
			SCTLogger.error(e.getMessage());
			// 创建新的
			try {
				col = new BXCollection(projectName, false, database);
			} catch (XMLDBException e1) {
				throw new RuntimeException(e1);
			}
		}
		return col;
	}
	
	/**
	 * 测试是否能够正确连接数据库
	 * 
	 * @return
	 */
	public synchronized boolean canConnect() {
		try {
			if (null != col && col.isOpen()) {
				return true;
			} else {
				return false;
			}
		} catch (XMLDBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 关闭连接
	 */
	public void stopDB() {
		try {
			if (null != col) {
				col.close();
				col = null;
			}
		} catch (XMLDBException e) {
			SCTLogger.error("关闭数据库错误：", e); //$NON-NLS-1$
		}
	}
	
	/**
	 * 清除数据库数据文件，以便恢复数据
	 */
	public boolean clearDBFiles() {
		boolean success = false;
		File file = new File(Constants.dbDir);
		success = FileManipulate.deleteDir(file);
		return success;
	}
	
	/**
	 * 数据库重置。若数据文件遭到破坏，导致XMLDB不能启动，可调用此方法
	 * 先清理，后启动。
	 */
	public void reset() {
		// 停止
		stopDB();
		// 清理
		clearDBFiles();
        // 启动
		startDB();
	}

	@Override
	public boolean backup() {
		return false;
	}

	@Override
	public DBXConnection getConnection() {
		return null;
	}

	@Override
	public DBXTransaction getTransaction() {
		return null;
	}

	@Override
	public int recover() {
		return 0;
	}

	@Override
	public boolean restore() {
		return false;
	}

	@Override
	public List<String> getCollectionDocs(String collName) {
		Collection col = getCollection(collName);
		if (col == null)
			return new ArrayList<String>();
		String[] docs = null;
		try {
			docs = col.listResources();
		} catch (XMLDBException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				col.close();
			} catch (XMLDBException e) {
				throw new RuntimeException(e);
			}
		}
		Arrays.sort(docs);
		return Arrays.asList(docs);
	}

	@Override
	public List<String> getCollections() {
		String query = " let $name := db:list() return $name";
		return XMLDBHelper.queryAttributes(query);
	}
}
