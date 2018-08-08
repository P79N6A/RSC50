/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.xmldb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.shrcn.found.common.Constants;
import com.shrcn.found.file.xml.XMLFileManager;
import com.shrcn.found.xmldb.bx.XMLDBHelperImpl;
import com.shrcn.found.xmldb.vtd.VTDCacheXMLDBHelper;
import com.shrcn.found.xmldb.vtd.XmlnsFindHandler;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-3-30
 */
/*
 * 修改历史 $Log: XMLDBHelper.java,v $
 * 修改历史 Revision 1.2  2013/04/07 12:24:57  cchun
 * 修改历史 Refactor:清理引用
 * 修改历史
 * 修改历史 Revision 1.1  2013/03/29 09:37:03  cchun
 * 修改历史 Add:创建
 * 修改历史
 * 修改历史 Revision 1.57  2012/11/26 08:38:17  cchun
 * 修改历史 Update:增加setProject()
 * 修改历史
 * 修改历史 Revision 1.56  2012/10/17 08:00:42  cchun
 * 修改历史 Refactor:改变静态变量初始化时机，便于测试
 * 修改历史
 * 修改历史 Revision 1.55  2012/09/03 07:19:59  cchun
 * 修改历史 Update:增加setNamespace()
 * 修改历史
 * 修改历史 Revision 1.53  2012/05/23 05:57:37  cchun
 * 修改历史 Refactor:去掉不必要的方法
 * 修改历史
 * 修改历史 Revision 1.52  2012/05/18 07:09:00  cchun
 * 修改历史 Update:整理注释
 * 修改历史
 * 修改历史 Revision 1.51  2012/04/19 10:29:23  cchun
 * 修改历史 Refactor:去掉接口loadDocFromStream()方法
 * 修改历史
 * 修改历史 Revision 1.50  2012/04/18 01:55:42  cchun
 * 修改历史 Fix Bug:修复数据库资源未释放的bug
 * 修改历史
 * 修改历史 Revision 1.49  2012/01/13 08:39:50  cchun
 * 修改历史 Refactor:为方便数据库切换改用新接口
 * 修改历史
 * 修改历史 Revision 1.48  2011/12/02 08:54:02  cchun
 * 修改历史 Update:添加exportFormatedDoc()
 * 修改历史
 * 修改历史 Revision 1.47  2010/12/20 02:40:19  cchun
 * 修改历史 Update:添加moveTo()，去掉数据库异常抛出
 * 修改历史
 * 修改历史 Revision 1.46  2010/11/08 02:47:16  cchun
 * 修改历史 Refactor:insertAfter()不再抛出异常
 * 修改历史
 * 修改历史 Revision 1.45  2010/10/11 05:14:32  cchun
 * 修改历史 Fix Bug:修复getAttributeString()数组越界异常
 * 修改历史
 * 修改历史 Revision 1.44  2010/09/08 08:28:03  cchun
 * 修改历史 Refactor:规范异常捕捉
 * 修改历史
 * 修改历史 Revision 1.43  2010/09/07 10:00:25  cchun
 * 修改历史 Update:更换过时接口
 * 修改历史
 * 修改历史 Revision 1.42  2010/08/18 08:29:44  cchun
 * 修改历史 Update:增加接口insertAfterType()
 * 修改历史
 * 修改历史 Revision 1.41  2010/05/06 02:27:16  cchun
 * 修改历史 Refactor:为removeDocument()添加collection参数
 * 修改历史
 * 修改历史 Revision 1.40  2010/04/06 01:34:33  cchun
 * 修改历史 Update
 * 修改历史
 * 修改历史 Revision 1.39  2010/03/16 12:16:58  cchun
 * 修改历史 Update: 更新
 * 修改历史
 * 修改历史 Revision 1.38  2009/12/09 07:16:09  cchun
 * 修改历史 Add:添加历史记录工具类
 * 修改历史
 * 修改历史 Revision 1.37  2009/08/18 09:36:13  cchun
 * 修改历史 Update:合并代码
 * 修改历史
 * 修改历史 Revision 1.34.2.4  2009/08/03 09:08:42  cchun
 * 修改历史 Update:添加信号关联上移、下移功能
 * 修改历史
 * 修改历史 Revision 1.34.2.3  2009/07/29 10:38:31  cchun
 * 修改历史 Update:修改Element加载方法，使其支持UTF-8字符集
 * 修改历史
 * 修改历史 Revision 1.34.2.2  2009/07/29 07:40:49  cchun
 * 修改历史 Update:完成方法
 * 修改历史 	public void insertNodeByDoc(Element element, String docName, String select);
 * 修改历史
 * 修改历史 	public void replaceNodeByDoc(Element element, String docName, String select);
 * 修改历史
 * 修改历史 Revision 1.34.2.1  2009/07/24 07:10:43  cchun
 * 修改历史 Update:切换数据库Sedna
 * 修改历史
 * 修改历史 Revision 1.34  2009/07/09 06:21:07  lj6061
 * 修改历史 删除外部事物处理的替换插入方法
 * 修改历史
 * 修改历史 Revision 1.33  2009/07/09 05:35:59  lj6061
 * 修改历史 添加通过Doc的插入和替换节点
 * 修改历史
 * 修改历史 Revision 1.32  2009/07/03 05:29:40  lj6061
 * 修改历史 修改replaceNode方法对于双引号套用单引号的字符串处理
 * 修改历史
 * 修改历史 Revision 1.31  2009/07/02 08:18:15  cchun
 * 修改历史 Refactor:XMLDBAdmin.openConatiner -> XMLDBAdmin.openContainer
 * 修改历史
 * 修改历史 Revision 1.30  2009/06/19 06:05:18  cchun
 * 修改历史 Update:合并代码
 * 修改历史
 * 修改历史 Revision 1.28.2.2  2009/06/19 05:57:04  cchun
 * 修改历史 修改appendAttribute()方法参数名和注释
 * 修改历史
 * 修改历史 Revision 1.28.2.1  2009/06/12 02:44:10  cchun
 * 修改历史 Fix Bug:修改with关键字缺少空格的bug
 * 修改历史
 * 修改历史 Revision 1.28  2009/06/01 01:43:43  wyh
 * 修改历史 添加由executeUpdate抛出的异常而增加的异常处理
 * 修改历史
 * 修改历史 Revision 1.27  2009/05/26 08:43:47  hqh
 * 修改历史 添加插入方法
 * 修改历史 修改历史 Revision 1.26 2009/05/25 08:23:38 cchun
 * 修改历史 添加采样值关联拖拽 修改历史 修改历史 Revision 1.25 2009/05/25 02:14:08 cchun 修改历史
 * Update:增加QueryContext线程安全处理 修改历史 修改历史 Revision 1.24 2009/05/20 02:39:03 cchun
 * 修改历史 Fix Bug:去掉xml字符串中的双引号 修改历史 修改历史 Revision 1.23 2009/05/19 08:25:58 cchun
 * 修改历史 Update:增加insertAsFirst() 修改历史 修改历史 Revision 1.22 2009/05/18 06:00:23
 * cchun 修改历史 Update:添加insertAsLast(),removeNodes()，修改replaceNode() 修改历史 修改历史
 * Revision 1.21 2009/05/15 08:12:48 cchun 修改历史
 * Update:添加公用方法insertAfter,replaceNode 修改历史 修改历史 Revision 1.20 2009/05/08
 * 12:14:35 hqh 修改历史 增加删除方法 修改历史 修改历史 Revision 1.19 2009/05/08 04:50:13 hqh 修改历史
 * 删除读日志 修改历史 修改历史 Revision 1.18 2009/05/08 02:34:59 wyh 修改历史
 * update:insertBefore() 修改历史 修改历史 Revision 1.17 2009/05/07 11:14:23 hqh 修改历史
 * 添加插入方法 修改历史 Revision 1.16 2009/04/22 12:20:20 cchun Update:添加报告属性保存方法
 * 
 * Revision 1.15 2009/04/21 02:55:35 lj6061 添加增加属性方法
 * 
 * Revision 1.14 2009/04/17 04:54:13 cchun Refactor:修改引用路径
 * 
 * Revision 1.13 2009/04/16 06:11:47 cchun Update:添加getAttributeValues()
 * 
 * Revision 1.11 2009/04/10 07:03:49 cchun Update:重构代码
 * 
 * Revision 1.10 2009/04/09 10:40:05 pht Update:selectSingleNode(),selectNodes()
 * 
 * Revision 1.9 2009/04/09 09:42:37 pht chenchun:去掉调试代码
 * 
 * Revision 1.8 2009/04/09 07:11:22 cchun Update:添加结果为xml node的查询方法
 * 
 * Revision 1.7 2009/04/07 02:01:22 cchun Update:添加注释
 * 
 * Revision 1.6 2009/04/02 08:35:07 cchun Update:增加接口
 * 
 * Revision 1.5 2009/04/02 03:16:16 cchun Update:更新代码
 * 
 * Revision 1.4 2009/04/01 08:09:56 cchun Update:完善数据操作接口
 * 
 * Revision 1.3 2009/04/01 05:37:47 cchun chenchun更新
 * 
 * Revision 1.2 2009/03/31 08:42:32 cchun xmlschema -> xsd
 * 
 * Revision 1.1 2009/03/31 08:24:52 cchun 添加基础服务项目文件
 * 
 */
public class XMLDBHelper {

	public static DBBroker broker = Constants.XQUERY ? new XMLDBHelperImpl() : new VTDCacheXMLDBHelper();
	public static List<String> lostNsList = null;
	
	public static void close() {
		broker.close();
	}
	
	public static void setProject(String project) {
		broker.setProject(project);
	}
	
	/**
	 * 快捷insert after操作。
	 * 
	 * @param select
	 * @param node
	 * @throws DriverException 
	 */
	public static void insertAfter(String select, Element node) {
		
		broker.insertAfter(select, node);
	}

	public static void insertAfterType(String parentXPath, String[] types, Element node) {
		
		broker.insertAfterType(parentXPath, types, node);
	}

	public static void insertAfterType(String parentXPath, String[] types, List<Element> nodes) {
		
		broker.insertAfterType(parentXPath, types, nodes);
	}

	/**
	 * 将DOM4J的节点添加至指定节点之前
	 * 
	 * @param txn
	 *            事务对象
	 * @param select
	 *            操作定位xpath
	 * @param node
	 *            DOM4J节点
	 */
	public static void insertBefore(String select, Element node) {
		
		broker.insertBefore(select, node);
	}

	/**
	 * 插入第一个子节点的快捷操作。
	 * 
	 * @param select
	 * @param node
	 * @throws DriverException 
	 */
	public static void insertAsFirst(String select, Element node) {
		
		broker.insertAsFirst(select, node);
	}

	/**
	 * 快捷插入一个节点至指定节点最后一个子节点之后。
	 * 
	 * @param select
	 * @param node
	 */
	public static void insertAsLast(String select, Element node) {
		
		broker.insertAsLast(select, node);
	}

	/**
	 * 将节点移动到上一个兄弟节点之前。
	 * @param select
	 * @param curRow
	 */
	public static void moveUp(String select, int curRow) {
		
		broker.moveUp(select, curRow);
	}
	
	/**
	 * 将节点移动到下一个兄弟节点之后。
	 * @param select
	 * @param curRow
	 */
	public static void moveDown(String select, int curRow) {
		
		broker.moveDown(select, curRow);
	}
	
	/**
	 * 移动到指定位置
	 * @param select
	 * @param curRow
	 * @param newRow
	 */
	public static void moveTo(String select, int curRow, int newRow) {
		
		broker.moveTo(select, curRow, newRow);
	}

	/**
	 * 快捷删除节点。
	 * 
	 * @param select
	 */
	public static void removeNodes(String select) {
		
		broker.deleteNodes(select);
	}

	/**
	 * 为指定节点添加一个新属性
	 * 
	 * @param select
	 *            操作定位xpath
	 * @param attrName
	 *            属性名称
	 * @param value
	 * 			  属性值
	 */
	public static void appendAttribute( String select,
			String attName, String value) {
		
		broker.appendAttribute(select, attName, value);
	}

	/**
	 * 节点替换。
	 * 
	 * @param select
	 * @param newNode
	 */
	public static void replaceNode(String select, Element newNode) {
		
		broker.replaceNode(select, newNode);
	}
	
	/**
	 * 修改指定节点或属性的值
	 * @param select
	 *            操作定位xpath
	 * @param value
	 *            新值
	 */
	public static void update(String select, String value) {
		broker.update(select, value);
	}
	
	public static void updateOnce(String select, String value) {
		broker.updateOnce(select, value);
	}

	/**
	 * 查询指定属性值(单值)。如果查询结果不唯一，则返回第一个值。
	 * @param select
	 *            属性xpath，例如:/a/b/@name
	 * @return 属性值
	 */
	public static String getAttributeValue(String select) {
		
		return broker.getAttributeValue(select);
	}

	/**
	 * 保存属性
	 * 
	 * @param ndXpath
	 *            属性所在节点xpath
	 * @param attName
	 *            属性名
	 * @param value
	 *            值
	 */
	public static void saveAttribute(String ndXpath, String attName, String value) {
		
		broker.saveAttribute(ndXpath, attName, value);
	}
	
	public static void saveAttributeValue(String attXpath, String value) {
		
		broker.saveAttributeValue(attXpath, value);		
	}

	/**
	 * 查询指定属性值(多值)。
	 * 
	 * @param select
	 *            属性xpath，例如:/a/b/@name
	 * @return 属性值
	 */
	public static List<String> getAttributeValues(String select) {
		
		return broker.getAttributeValues(select);
	}
	
	/**
	 * 根据xquery查询属性值列表
	 * @param xquery
	 * @return
	 */
	public static List<String> getAttributeStrings(String xquery) {
		
		return broker.getAttributeStrings(xquery);
	}
	
	/**
	 * 根据xquery查询属性值
	 * @param xquery
	 * @return
	 */
	public static String getAttributeString(String xquery) {
		
		return broker.getAttributeString(xquery);
	}

	/**
	 * 查询指定节点值
	 * @param select 属性xpath，例如:/a/b
	 * @return 节点值
	 */
	public static String getNodeValue(String select) {
		
		return broker.getNodeValue(select);
	}

	public static Element selectSingleNode(String select) {
		
		return broker.selectSingleNode(select);
	}

	public static List<Element> selectNodes(String select) {
		
		return broker.selectNodes(select);
	}

	public static List<Element> selectNodesOnlyAtts(String select, String ndName) {
		
		return broker.selectNodesOnlyAtts(select, ndName);
	}
	
	public static List<Element> selectNodes(String select, int offset, int number) {
		
		return broker.selectNodes(select, offset, number);
	}

	/**
	 * 插入
	 * 
	 * @param root
	 * @param xpath
	 */
	public static void insert(String xpath, Element root) {
		
		broker.insert(xpath, root);
	}
	
	public static List<Element> queryNodes(String xquery) {
		
		return broker.queryNodes(xquery);
	}
	
	public static List<String> queryAttributes(String xquery) {
		return broker.queryAttributes(xquery);
	}
	
	public static String queryAttribute(String xquery) {
		
		return broker.queryAttribute(xquery);
	}
	
	public static boolean existsNode(String xpath) {
		
		return broker.existsNode(xpath);
	}
	
	public static boolean isAutoCommit() {
		return broker.isAutoCommit();
	}

	public static void setAutoCommit(boolean autoCommit) {
		broker.setAutoCommit(autoCommit);
	}
	
	public static void forceBegin() {
		broker.forceBegin();
	}

	public static void forceCommit() {
		broker.forceCommit();
	}

	public static void forceRollback() {
		broker.forceRollback();
	}
	
	public static void exportFormatedDoc(String projectName, String docName, String outPath) {
		
		broker.exportFormatedDoc(projectName, docName, outPath);
	}
	
	public static void exportFormatedDoc(String outPath) {
		
		broker.exportFormatedDoc(outPath);
	}
	
	public static void loadDocument(String docName, String filePath) {
		broker.loadDocument(docName, filePath);
		lostNsList = null;
	}
	
	public static void removeDocument(String docName, String colName) {
		broker.removeDocument(docName, colName);
	}
	
	public static int countNodes(String select) {
		return broker.countNodes(select);
	}

	public static void loadElementAsDocument(String docName, Element node) {
		broker.loadElementAsDocument(docName, node);
		lostNsList = null;
	}

	public static String getDocXPath() {
		return broker.getDocXPath();
	}
	
	public static void setNamespace(String ns){
		 broker.setNamespace(ns);
	}
	
	public static Object callFunction(Class<?> funClass, Map<String, Object> params) {
		return broker.createFunction(funClass, params).exec();
	}
	
	public static Object callUpdateFunction(Class<?> funClass, Map<String, Object> params) {
		return broker.createUpdateFunction(funClass, params).exec();
	}
	
	public static boolean isInCache(String select) {
		return broker.isInCache(select);
	}
	
	/**
	 *  将查询结果转成dom4j节点集合
	 * @param pr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> getNodesFromResult(String pr) {
		Element root = null;
		StringBuilder content = new StringBuilder("<content>"); //$NON-NLS-1$
		content.append(pr);
		content.append("</content>"); //$NON-NLS-1$
		try {
			root = getElementFromStr(content.toString());
		} catch (DocumentException e) {
			try {
				ByteArrayInputStream fis = new ByteArrayInputStream(content.toString().getBytes(Constants.CHARSET_UTF8));
				XmlnsFindHandler handler = new XmlnsFindHandler();
				XMLFileManager.parseUTF8BySax(fis, handler);
				String ns = "";
				if (lostNsList == null)
					lostNsList = new ArrayList<>();
				lostNsList.addAll(handler.getLostNs());
				List<String> contentNsList = new ArrayList<>();
				for (String lostNs : lostNsList) {
					int p = lostNs.indexOf('_');
					if (p >  0)
						lostNs = lostNs.substring(0, p);
					if (!contentNsList.contains(lostNs))
						ns += " xmlns:" + lostNs + "=\"http://www." + lostNs + ".com\"";
					contentNsList.add(lostNs);
				}
				root = getElementFromStr("<content" + ns + ">" + pr + "</content>");
				fis.close();
			} catch (DocumentException | IOException e1) {
				throw new RuntimeException(e1);
			}
		}
		return root.elements();
	}

	private static Element getElementFromStr(String content)
			throws DocumentException {
		Document doc = DocumentHelper.parseText(content);
		return doc.getRootElement();
	}
	
	public static void resetLostNs() {
		if (lostNsList != null)
			lostNsList.clear();
		lostNsList = null;
	}
}