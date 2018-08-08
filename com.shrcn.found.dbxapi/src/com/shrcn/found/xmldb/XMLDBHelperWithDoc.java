/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.found.common.Constants;
import com.shrcn.found.xmldb.bx.XMLDBHelperImpl;
import com.shrcn.found.xmldb.vtd.VTDCacheXMLDBHelper;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-3-29
 */
/**
 * $Log: XMLDBHelperWithDoc.java,v $
 * Revision 1.2  2013/04/07 12:24:57  cchun
 * Refactor:清理引用
 *
 * Revision 1.1  2013/03/29 09:37:03  cchun
 * Add:创建
 *
 * Revision 1.6  2012/11/26 08:38:17  cchun
 * Update:增加setProject()
 *
 * Revision 1.5  2012/10/17 08:00:42  cchun
 * Refactor:改变静态变量初始化时机，便于测试
 *
 * Revision 1.4  2012/05/23 05:57:37  cchun
 * Refactor:去掉不必要的方法
 *
 * Revision 1.3  2012/05/18 07:09:01  cchun
 * Update:整理注释
 *
 * Revision 1.2  2012/04/19 10:27:43  cchun
 * Update:添加close()
 *
 * Revision 1.1  2012/01/13 08:39:51  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 * Revision 1.16  2011/03/21 07:08:30  cchun
 * Update:existXX()方法添加对doc是否存在的判断
 *
 * Revision 1.15  2010/10/08 03:26:10  cchun
 * Update:增加在节点前插入操作
 *
 * Revision 1.14  2010/06/28 09:49:54  cchun
 * Update:添加文件导出方法
 *
 * Revision 1.13  2010/06/24 09:22:12  cchun
 * Update:添加queryAttributes()
 *
 * Revision 1.12  2010/05/17 01:34:15  cchun
 * Update:增加replaceAttributeValue()
 *
 * Revision 1.11  2010/05/10 05:24:50  cchun
 * Update:添加selectNodes()查询指定序号内的节点
 *
 * Revision 1.10  2010/04/28 11:10:07  cchun
 * Update:saveAttributeValue()时处理xml转义符
 *
 * Revision 1.9  2010/04/28 08:14:12  cchun
 * Fix Bug:修复insertAsFirst()
 *
 * Revision 1.8  2010/04/28 03:51:24  cchun
 * Update:去掉不必要的事务回滚操作
 *
 * Revision 1.7  2010/04/26 06:46:47  cchun
 * Update:改进事务控制
 *
 * Revision 1.6  2010/04/23 01:36:58  cchun
 * Update:添加数据库访问同步
 *
 * Revision 1.5  2010/04/21 06:46:17  cchun
 * Update:去掉同步
 *
 * Revision 1.4  2010/04/07 05:11:49  cchun
 * Update:添加getNodeValue（）
 *
 * Revision 1.3  2010/04/06 01:34:31  cchun
 * Update
 *
 * Revision 1.1  2010/03/30 08:31:00  cchun
 * Update:添加系统对话框保存事件
 *
 */
public class XMLDBHelperWithDoc {

	public static DBBroker broker = Constants.XQUERY ? new XMLDBHelperImpl() : null;
	
	private static Map<String, DBBroker> brokerMap = new HashMap<String, DBBroker>();
	
	private static DBBroker getDBBroker(String docName) {
		if (Constants.XQUERY) {
			return broker;
		} else {
			DBBroker docBroker = brokerMap.get(docName);
			if (docBroker == null) {
				docBroker = new VTDCacheXMLDBHelper();
				brokerMap.put(docName, docBroker);
			}
			return docBroker;
		}
	}
	
	public static void loadDocument(String docName, String filePath) {
		getDBBroker(docName).loadDocument(docName, filePath);
	}
	
	public static void removeDocument(String docName, String colName) {
		if (Constants.XQUERY) {
			getDBBroker(docName).removeDocument(docName, colName);
		} else {
			DBBroker broker = brokerMap.get(docName);
			if (broker != null)
				broker.close();
			brokerMap.remove(docName);
		}
	}
	
	public static void close() {
		if (Constants.XQUERY) {
			if (broker != null)
				broker.close();
		} else {
			if (brokerMap.size() > 0) {
				for (DBBroker broker : brokerMap.values()) {
					broker.close();
				}
				brokerMap.clear();
			}
		}
	}
	
	public static void setProject(String project) {
		if (Constants.XQUERY) {
			if (broker != null)
				broker.setProject(project);
		} else {
			if (brokerMap.size() > 0) {
				for (DBBroker broker : brokerMap.values()) {
					broker.setProject(project);
				}
			}
		}
	}
	
	/**
	 * 将指定节点用新节点替换。
	 * @param select 待替换节点xpath
	 * @param node xml节点
	 */
	public static void replaceNode(String docName, String select, Element node) {
		getDBBroker(docName).setDocument(docName);
		getDBBroker(docName).replaceNode(select, node);
	}
	
	/**
	 * 判断指定节点是否存在。
	 * @param select xpath
	 * @return 存在为true，否则为false。
	 */
	public static boolean existsNode(String docName, String select) {
		getDBBroker(docName).setDocument(docName);
		return getDBBroker(docName).existsNode(select);
	}
	
	/**
	 * 向指定节点插入一个子节点（位置随机）。
	 * @param select 插入位置xpath
	 * @param node xml节点
	 */
	public static void insert(String docName, String select, Element node) {
		getDBBroker(docName).setDocument(docName);
		getDBBroker(docName).insert(select, node);
	}
	
	/**
	 * 向指定节点插入一个子节点（位置随机）。
	 * @param select 插入位置xpath
	 * @param content xml节点字符串
	 */
	public static void insert(String docName, String select, String content) {
		getDBBroker(docName).setDocument(docName);
		getDBBroker(docName).insert(select, content);
	}
	
	/**
	 * 保存一个子节点（位置随机）到指定位置。
	 * @param select 插入位置xpath
	 * @param node xml节点
	 */
	public static void saveAsChild(String docName, String select, Element node) {
		String ndXpath = select + "/" + node.getName();
		if(existsNode(docName, ndXpath)) {
			replaceNode(docName, ndXpath, node);
		} else {
			insert(docName, select, node);
		}
	}
	
	/**
	 * 向指定节点之后插入一个新节点。
	 * @param select 插入位置xpath
	 * @param node xml节点
	 */
	public static void insertAfter(String docName, String select, Element node) {
		getDBBroker(docName).setDocument(docName);
		getDBBroker(docName).insertAfter(select, node);
	}
	
	/**
	 * 向指定节点之前插入一个新节点。
	 * @param select 插入位置xpath
	 * @param node xml节点
	 */
	public static void insertBefore(String docName, String select, Element node) {
		getDBBroker(docName).setDocument(docName);
		getDBBroker(docName).insertBefore(select, node);
	}
	
	/**
	 * 向指定节点第一个子节点位置插入一个新节点。
	 * @param select 插入位置xpath
	 * @param node xml节点
	 */
	public static void insertAsFirst(String docName, String select, Element node) throws Exception {
		getDBBroker(docName).setDocument(docName);
		getDBBroker(docName).insertAsFirst(select, node);
	}
	
	/**
	 * 向指定节点最后一个子节点位置插入一个新节点。
	 * @param select 插入位置xpath
	 * @param node xml节点
	 */
	public static void insertAsLast(String docName, String select, Element node) {
		getDBBroker(docName).setDocument(docName);
		getDBBroker(docName).insertAsLast(select, node);
	}
	
	/**
	 * 统计指定节点个数
	 * @param docName
	 * @param select
	 * @return
	 */
	public static int countNodes(String docName, String select) {
		getDBBroker(docName).setDocument(docName);
		return getDBBroker(docName).countNodes(select);
	}
	
	/**
	 * 将指定节点删除。
	 * @param select 待删除节点xpath
	 */
	public static void deleteNodes(String docName, String select) {
		getDBBroker(docName).setDocument(docName);
		getDBBroker(docName).deleteNodes(select);
	}
	
	/**
	 * 根据xapth返回唯一节点。如查询结果不唯一，则返回第一个节点。
	 * @param select xpath
	 * @return
	 */
	public static Element selectSingleNode(String docName, String select) {
		getDBBroker(docName).setDocument(docName);
		return getDBBroker(docName).selectSingleNode(select);
	}
	
	/**
	 * 执行查询，返回xpath对应的节点集合。
	 * @param select xpath
	 * @return
	 */
	public static List<Element> selectNodes(String docName, String select) {
		getDBBroker(docName).setDocument(docName);
		return getDBBroker(docName).selectNodes(select);
	}
	
	/**
	 * 查询指定路径下指定区间内的节点
	 * @param docName
	 * @param select
	 * @param offset
	 * @param number
	 * @return
	 */
	public static List<Element> selectNodes(String docName, String select, int offset, int number) {
		getDBBroker(docName).setDocument(docName);
		return getDBBroker(docName).selectNodes(select, offset, number);
	}
	
	/**
	 * 查询节点值
	 * @param select
	 * @return
	 */
	public static String getNodeValue(String docName, String select) {
		getDBBroker(docName).setDocument(docName);
		return getDBBroker(docName).getNodeValue(select);
	}
	
	/**
	 * 查询指定doc下xpath指定的单一属性值
	 * @param docName
	 * @param select
	 * @return
	 */
	public static String getAttributeValue(String docName, String select) {
		getDBBroker(docName).setDocument(docName);
		return getDBBroker(docName).getAttributeValue(select);
	}
	
	/**
	 * 查询指定doc下xpath指定的所有属性值
	 * @param select
	 * @return
	 */
	public static List<String> getAttributeValues(String docName, String select){
		getDBBroker(docName).setDocument(docName);
		return getDBBroker(docName).getAttributeValues(select);
	}
	
	/**
	 * 保存节点属性
	 * @param docName
	 * @param ndXpath
	 * @param attName
	 * @param value
	 */
	public static void saveAttribute(String docName, String ndXpath, String attName, String value) {
		getDBBroker(docName).setDocument(docName);
		getDBBroker(docName).saveAttribute(ndXpath, attName, value);
	}
	
	public static void saveAttributeValue(String docName, String attXpath, String value) {
		getDBBroker(docName).setDocument(docName);
		getDBBroker(docName).saveAttributeValue(attXpath, value);
	}

	public static String getDocXPath(String docName) {
		getDBBroker(docName).setDocument(docName);
		return getDBBroker(docName).getDocXPath();
	}

	public static List<Element> queryNodes(String docName, String xquery) {
		return getDBBroker(docName).queryNodes(xquery);
	}
	
	/**
	 * 根据xquery查询属性值列表
	 * @param xquery
	 * @return
	 */
	public static List<String> getAttributeStrings(String docName, String xquery) {
		return getDBBroker(docName).getAttributeStrings(xquery);
	}
	
	/**
	 * 根据xquery查询属性值
	 * @param xquery
	 * @return
	 */
	public static String getAttributeString(String docName, String xquery) {
		return getDBBroker(docName).getAttributeString(xquery);
	}
	
	public static List<String> queryAttributes(String docName, String xquery) {
		return getDBBroker(docName).queryAttributes(xquery);
	}
	
	public static String queryAttribute(String docName, String xquery) {
		return getDBBroker(docName).queryAttribute(xquery);
	}
}
