/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb;

import java.util.List;

import org.xmldb.api.base.Collection;

import com.shrcn.found.xmldb.bx.XMLDBAdminImpl;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-1-11
 */
/**
 * $Log: XMLDBAdmin.java,v $
 * Revision 1.2  2013/04/07 12:24:57  cchun
 * Refactor:清理引用
 *
 * Revision 1.1  2013/03/29 09:37:02  cchun
 * Add:创建
 *
 * Revision 1.30  2012/10/17 08:00:42  cchun
 * Refactor:改变静态变量初始化时机，便于测试
 *
 * Revision 1.29  2012/01/13 08:39:52  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 */
public class XMLDBAdmin {
	
	public static DBAdminBroker admin = new XMLDBAdminImpl();
	
	public static boolean startDB(){
		return admin.startDB();
	}
	
	public static void stopDB(){
		admin.stopDB();
	}
	
	public static Collection getCollection(){
		return admin.getCollection();
	}
	
	public static Collection getCollection(String projectName){
		return admin.getCollection(projectName);
	}
	
	public static DBXConnection getConnection(){
		return admin.getConnection();
	}
	
	public static DBXTransaction getTransaction(){
		return admin.getTransaction();
	}
	
	public static boolean canConnect(){
		return admin.canConnect();
	}
	
	public static boolean clearDBFiles(){
		return admin.clearDBFiles();
	}
	
	public static boolean backup(){
		return admin.backup();
	}
	
	public static boolean restore(){
		return admin.restore();
	}
	
	public static int recover(){
		return admin.recover();
	}
	
	public static void reset(){
		admin.reset();
	}
	
	public static List<String> getCollections() {
		return admin.getCollections();
	}

	public static List<String> getCollectionDocs(String collName) {
		return admin.getCollectionDocs(collName);
	}
}
