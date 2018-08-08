/**

 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.xmldb;

import java.util.List;
import java.util.Map;

import org.dom4j.Element;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-7-21
 */

public interface DBBroker {
	
	public void close();
	
	/**
	 * 获取命名空间名称
	 * @return
	 */
	public String getNamespace();
	public void setNamespace(String ns);
	
	/**
	 * 获取xml文档名
	 * @return
	 */
	public String getDocument();
	public void setDocument(String doc);
	
	public String getProject();
	public void setProject(String project);
	
	/**
	 * 获取document xpath。
	 * @param docName
	 * @return
	 */
	public String getDocXPath();
	
	/**
	 * 判断指定文件是否存在
	 * @param docName 文档标识
	 * @return 存在为true，否则为false。
	 */
	public boolean existsDocument(String docName);
	
	/**
	 * 加载document
	 * @param docName 文档标识
	 * @param filePath xml文件路径
	 * @throws DriverException 
	 */
	public void loadDocument(String docName, String filePath);
	
	/**
	 * 从dom4j Element加载document
	 * @param docName 文档标识
	 * @param node dom4j Element
	 */
	public void loadElementAsDocument(String docName, Element node);
	
	/**
	 * 从xml字符串加载document
	 * @param docName
	 * @param xmlStr
	 */
	public void loadDocFromString(String docName, String xmlStr);
	
//	/**
//	 * 从指定输入流加载document
//	 * @param docName
//	 * @param is
//	 */
//	public void loadDocFromStream(String docName, InputStream is);
//
//	/**
//	 * 从指定资源加载document
//	 * @param docName
//	 * @param uri
//	 */
//	public void loadDocFromURI(String docName, String uri);
	
	/**
	 * 删除document
	 * @param docName 文档标识
	 * @throws DriverException 
	 */
	public void removeDocument(String docName, String colName);
	
	/**
	 * 导出xml文件
	 * @param projectName
	 * @param outPath
	 */
	public void exportFormatedDoc(String projectName, String docName, String outPath);
	
	/**
	 * 导出xml文件
	 * @param outPath
	 */
	public void exportFormatedDoc(String outPath);
	
	/**
	 * 执行查询，返回xpath对应的节点集合。
	 * @param select xpath
	 * @return
	 */
	public List<Element> selectNodes(String select);
	
	public List<Element> selectNodesOnlyAtts(String select, String ndName);
	
	public List<Element> selectNodes(String select, int offset, int number);
	
	/**
	 * 根据xapth返回唯一节点。如查询结果不唯一，则返回第一个节点。
	 * @param select xpath
	 * @return
	 */
	public Element selectSingleNode(String select);
	
	/**
	 * 判断指定节点是否存在。
	 * @param select xpath
	 * @return 存在为true，否则为false。
	 */
	public boolean existsNode(String select);
	
	public int countNodes(String select);
	
	/**
	 * 向指定节点之前插入一个新节点。
	 * @param select 插入位置xpath
	 * @param node xml节点
	 */
	public void insertBefore(String select, Element node);
	public void insertBefore(String select, String content);
	
	/**
	 * 向指定节点之后插入一个新节点。
	 * @param select 插入位置xpath
	 * @param node xml节点
	 */
	public void insertAfter(String select, Element node);
	public void insertAfter(String select, String content);
	
	/**
	 * 向指定节点类型之后添加子节点
	 * @param parentXPath
	 * @param types
	 * @param node
	 */
	public void insertAfterType(String parentXPath, String[] types, Element node);
	
	public void insertAfterType(String parentXPath, String[] types, String node);
	
	public void insertAfterType(String parentXPath, String[] types, List<Element> nodes);
	
	/**
	 * 向指定节点第一个子节点位置插入一个新节点。
	 * @param select 插入位置xpath
	 * @param node xml节点
	 */
	public void insertAsFirst(String select, Element node);
	public void insertAsFirst(String select, String content);
	
	/**
	 * 向指定节点最后一个子节点位置插入一个新节点。
	 * @param select 插入位置xpath
	 * @param node xml节点
	 */
	public void insertAsLast(String select, Element node);
	public void insertAsLast(String select, String content);
	
	/**
	 * 将节点移动到上一个兄弟节点之前。
	 * @param select
	 * @param curRow
	 */
	public void moveUp(String select, int curRow);
	
	/**
	 * 将节点移动到下一个兄弟节点之后。
	 * @param select
	 * @param curRow
	 */
	public void moveDown(String select, int curRow);
	
	/**
	 * 将节点移动到指定位置
	 * @param select
	 * @param curRow
	 * @param newRow
	 */
	public void moveTo(String select, int curRow, int newRow);
	
	/**
	 * 向指定节点插入一个子节点（位置随机）。
	 * @param select 插入位置xpath
	 * @param node xml节点
	 */
	public void insert(String select, Element node);

	public void insert(String select, String content);

	/**
	 * 将指定节点用新节点替换。
	 * @param select 待替换节点xpath
	 * @param node xml节点
	 */
	public void replaceNode(String select, Element node);
	
	public void replaceNode(String select, String node);
	
	/**
	 * 将指定节点删除。
	 * @param select 待删除节点xpath
	 */
	public void deleteNodes(String select);
	
	/**
	 * 保存属性至指定节点。
	 * @param ndXpath 节点xpath
	 * @param attName 属性名
	 * @param value 值
	 */
	public void saveAttribute(String ndXpath, String attName, String value);
	
	/**
	 * 保存属性。
	 * @param attXpath 例如：aa/bb/@id
	 * @param value
	 */
	public void saveAttributeValue(String attXpath, String value);

	/**
	 * 获取单个属性值
	 * @param select 属性xpath
	 * @return 值字符串
	 */
	public String getAttributeValue(String select);
	
	/**
	 * 获取多个属性值
	 * @param select 属性xpath
	 * @return 字符串集合
	 */
	public List<String> getAttributeValues(String select);
	
	/**
	 * 获取多个属性值
	 * @param xquery XQuery语句
	 * @return
	 */
	public List<String> getAttributeStrings(String xquery);

	/**
	 * 获取单个属性值。
	 * @param xquery
	 * @return
	 */
	public String getAttributeString(String xquery);
	
	/**
	 * 获取指定节点值
	 * @param select 节点xpath
	 * @return 值字符串
	 */
	public String getNodeValue(String select);
	
	/**
	 * 向指定节点添加一个属性
	 * @param ndXpath 节点xpath
	 * @param attName 属性名称
	 * @param value 属性值
	 */
	public void appendAttribute(String ndXpath, String attName, String value);

	/**
	 * 执行一条XQuery查询语句，将结果以List&lt;Element&gt;形式返回。
	 * @param xquery 任意XQuery查询语句
	 * @return
	 */
	public List<Element> queryNodes(String xquery);
	
	/**
	 * 执行一条XQuery查询语句，将结果以List&lt;String&gt;形式返回。
	 * @param xquery 任意XQuery查询语句
	 * @return
	 */
	public List<String> queryAttributes(String xquery);
	
	/**
	 * 执行一条XQuery查询语句，将结果以String形式返回。
	 * @param xquery 任意XQuery查询语句
	 * @return
	 */
	public String queryAttribute(String xquery);

	/**
	 * 指定一条XQuery更新语句。
	 * @param xupdate
	 * @return
	 * @throws DriverException
	 */
	public int executeUpdate(String xupdate);
	
	/**
	 * 更新节点值
	 * @param select 节点xpath
	 * @param value 值
	 * @return
	 */
	public void update(String select, String value);

	/**
	 * 更新节点值（vtd专用，使用一次后必须forceCommit()，否则出错）
	 * @param select 节点xpath
	 * @param value 值
	 * @return
	 */
	public void updateOnce(String select, String value);
	
	public boolean isAutoCommit();

	public void setAutoCommit(boolean autoCommit);
	
	public void forceBegin();
	public void forceCommit();
	public void forceRollback();

	public void insertNodeByDoc(Element element, String docName, String select);

	public void replaceNodeByDoc(Element element, String docName, String select);
	
	public IFunction createFunction(Class<?> funClass, Map<String, Object> params);

	public IFunction createUpdateFunction(Class<?> funClass, Map<String, Object> params);
	
	public boolean isInCache(String select);
}
