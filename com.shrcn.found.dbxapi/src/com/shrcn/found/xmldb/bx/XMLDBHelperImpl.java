/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.bx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.basex.api.xmldb.XMLResource;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.Delete;
import org.basex.core.cmd.Open;
import org.basex.core.cmd.XQuery;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.file.xml.XMLFileManager;
import com.shrcn.found.xmldb.AbstractDBBroker;
import com.shrcn.found.xmldb.XMLDBAdmin;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-1-11
 */
/**
 * $Log: XMLDBHelperImpl.java,v $
 * Revision 1.4  2013/12/30 01:15:56  cchun
 * Fix Bug:1、保存溢出；2、属性更新缺少判断
 *
 * Revision 1.3  2013/04/24 02:35:52  cchun
 * Refactor:移动saveUTF8Document()到FileManager
 *
 * Revision 1.2  2013/04/07 12:24:56  cchun
 * Refactor:清理引用
 *
 * Revision 1.1  2013/03/29 09:38:31  cchun
 * Add:创建
 *
 * Revision 1.16  2012/11/26 08:38:41  cchun
 * Update:增加setProject(),getProject()
 *
 * Revision 1.15  2012/10/17 08:01:13  cchun
 * Update:初始化时设置doc
 *
 * Revision 1.14  2012/08/28 03:52:38  cchun
 * Update:清理引用
 *
 * Revision 1.13  2012/08/21 03:31:54  cchun
 * Update:清理引用
 *
 * Revision 1.12  2012/07/30 09:51:14  cchun
 * Update:改成context.close()
 *
 * Revision 1.11  2012/05/23 05:56:13  cchun
 * Refactor:修改executeQuery()方法参数，调整异常处理
 *
 * Revision 1.10  2012/05/18 07:08:28  cchun
 * Update:添加Collection关闭逻辑
 *
 * Revision 1.9  2012/04/19 10:30:11  cchun
 * Fix Bug:修复数据库资源未释放的bug
 *
 * Revision 1.8  2012/04/18 01:55:43  cchun
 * Fix Bug:修复数据库资源未释放的bug
 *
 * Revision 1.7  2012/04/16 03:46:50  cchun
 * Fix Bug:修复context对象冲突bug
 *
 * Revision 1.6  2012/03/19 08:47:50  cchun
 * Fix Bug:修复insertAfterType() xpath错误和逻辑错误
 *
 * Revision 1.5  2012/03/09 06:18:05  cchun
 * Fix Bug:修复saveAttribute()不能批量更新的bug
 *
 * Revision 1.4  2012/02/07 08:39:33  cchun
 * Fix Bug:去掉selectSingleNode()多余的根结点，避免输出错误
 *
 * Revision 1.3  2012/02/01 03:24:15  cchun
 * Fix Bug:修复exeuteUpdate()错误
 *
 * Revision 1.2  2012/01/31 01:38:33  cchun
 * Fix Bug:修复Context没有关闭的bug
 *
 * Revision 1.1  2012/01/13 08:39:48  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 */
public class XMLDBHelperImpl extends AbstractDBBroker {
	
	private Context context = new Context();
	
	public XMLDBHelperImpl() {
		if (context == null) {
			this.context = new Context();
		}
		setDocument(null);
	}

	@Override
	public void setDocument(String doc) {
		if (doc == null || Constants.DEFAULT_SCD_DOC_NAME.equals(doc)) {
			this.document = Constants.DEFAULT_SCD_DOC_NAME;
			setNamespace("declare default element namespace 'http://www.iec.ch/61850/2003/SCL'; ");
		} else {
			this.document = doc;
			setNamespace("");
		}
	}
		
	/**
	 * 判断指定文件是否存在
	 * 
	 * @param docName 文档标识
	 *           
	 * @return 存在为true，否则为false。
	 */
	public boolean existsDocument(String docName) {
		if (getProject() == null || docName == null)
			return false;
		boolean exist = false;
		Collection col = XMLDBAdmin.getCollection();
		if (col != null) {
			String[] resources ={""};
			try {
				resources = col.listResources();
				for (int i = 0; i < resources.length; i++) {
					if (docName.equals(resources[i])) {
						exist = true;
					}
				}
			} catch (XMLDBException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					col.close();
				} catch (XMLDBException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return exist;
	}
	
	
	
	/**
	 * 加载document
	 * @param docName 文档标识
	 * @param filePath xml文件路径
	 * @throws DriverException 
	 */
	public void loadDocument(String docName, String filePath) {
		Context context = new Context();
		try {
			new Open(getProject()).execute(context);
			new Add(docName, filePath).execute(context);
		} catch (BaseXException e) {
			throw new RuntimeException(e);
		} finally {
			context.close();
		}
	}
	
	/**
	 * 删除document
	 * @param docName 文档标识
	 * @throws DriverException 
	 */
	public void removeDocument(String docName, String colName) {
		Context context = new Context();
		try {
			new Open(colName).execute(context);
			new Delete(docName).execute(context);
		} catch (BaseXException e) {
			throw new RuntimeException(e);
		} finally {
			context.close();
		}
		
	}
	
	/**
	 * 导出数据库中projectName下指定的SCD document
	 * @param projectName
	 * @param outPath
	 */
	public synchronized void exportFormatedDoc(String projectName, String docName, String outPath) {
		release();
		Collection col = null;
		try {
			col = XMLDBAdmin.getCollection(projectName);
			XMLResource resource = (XMLResource) col.getResource(docName);
			resource.saveContent(outPath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (null != col)
					col.close();
				recover();
			} catch (XMLDBException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * 导出xml文件
	 * 
	 * @param outPath
	 */
	public synchronized void exportFormatedDoc(String outPath) {
		release();
		Collection col = null;
		try {
			col = XMLDBAdmin.getCollection(); 
			XMLResource resource = (XMLResource) col.getResource(Constants.DEFAULT_SCD_DOC_NAME);
			resource.saveContent(outPath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (col != null)
					col.close();
				recover();
			} catch (XMLDBException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * 执行查询，返回xpath对应的节点集合。
	 * @param select xpath
	 * @return
	 */
	public List<Element> selectNodes(String select) {
		select = DOM4JNodeHelper.clearPrefix(select);
		String query = "let $doc := " + getDocXPath()
				+ " return $doc" + select;
		return queryNodes(query);
	}
	
	public List<Element> selectNodesOnlyAtts(String select, String ndName) {
		select = DOM4JNodeHelper.clearPrefix(select);
		String fnQuery = "for $nd in " + XMLDBHelper.getDocXPath() + select
				+ " return element " + ndName  + " {$nd/@*}";
		return queryNodes(fnQuery);
	}
	
	@Override
	public List<Element> selectNodes(String select, int offset, int number) {
		String ndXPath = getDocXPath() + select;
		String xquery = "for $nd at $i in " + ndXPath + " where $i>=" + (offset + 1)
			+ " and $i<" + (offset + number + 1) + " return $nd";
		return queryNodes(xquery);
	}
	
	/**
	 * 根据select返回唯一节点。如查询结果不唯一，则返回第一个节点。
	 * 
	 * @param select
	 *           
	 * @return
	 */
	public Element selectSingleNode(String select) {
		List<Element> nodes = selectNodes(select);
		if (nodes.size() > 0) {
			Element nd = (Element)nodes.get(0).detach();
			DocumentHelper.createDocument(nd);
			return nd;
		} else {
			return null;
		}
	}
	
	/**
	 * 判断指定节点是否存在。
	 * @param select 
	 * @return 存在为true，否则为false。
	 */
	public boolean existsNode(String select) {
		if (getDocument() == null
				|| getProject() == null)
			return false;
		select = DOM4JNodeHelper.clearPrefix(select);
		boolean exist = false;
		if (countNodes(select) != 0)
			exist = true;
		return exist;
	}
	
	/**
	 * 向指定节点之前插入一个新节点。
	 * @param select 插入位置xpath
	 * @param node xml节点
	 */
	public void insertBefore(String select, Element node) {
		DOM4JNodeHelper.clearDefaultNS(node);
		insertBefore(select, node.asXML());
	}
	
	/**
	 * 向指定节点之后插入一个新节点。
	 * @param select 插入位置xpath
	 * @param node xml节点
	 */
	public void insertAfter(String select, Element node) {
		DOM4JNodeHelper.clearDefaultNS(node);
		insertAfter(select, node.asXML());
	}
	
	/**
	 * 向指定节点类型之后添加子节点
	 * @param parentXPath
	 * @param types
	 * @param node
	 */
	public void insertAfterType(String parentXPath, String[] types, Element node) {
		List<Element> nodes = new ArrayList<>();
		nodes.add(node);
		insertAfterType(parentXPath, types, nodes);
	}
	
	public void insertAfterType(String parentXPath, String[] types, List<Element> nodes) {
		StringBuilder sbVarSibles = new StringBuilder();
		for(int i = 0; i < types.length; i++) {
			String type = types[i];
			sbVarSibles.append("$p/scl:" + type);
			if(i < types.length - 1)
				sbVarSibles.append(", ");
		}
		 String targetPath = "let $doc:=" + getDocXPath() +
		    ", $p:=$doc"+ parentXPath + ", $children:=(" + sbVarSibles.toString() +
		    ") return $children[last()]";
		List<Element> targetNode = XMLDBHelper.queryNodes(targetPath);
		if(targetNode.isEmpty()) {
			for (Element node : nodes) {
				insertAsFirst(parentXPath, node);
			}
		} else {
			Element lastNode = targetNode.get(targetNode.size()-1);
			String nodeName = lastNode.getName();
			targetPath = parentXPath + "/*[name()='" + nodeName + "'][last()]";
			for (Element node : nodes) {
				insertAfter(targetPath, node);
			}
		}
	}
	
	@Deprecated
	public void insertAfterType(String parentXPath, String[] types, String node) {
		StringBuilder sbVarSibles = new StringBuilder();
		for(int i = 0; i < types.length; i++) {
			String type = types[i];
			sbVarSibles.append("$p/scl:" + type);
			if(i < types.length - 1)
				sbVarSibles.append(", ");
		}
		 String targetPath = "let $doc:=" + getDocXPath() +
		    ", $p:=$doc"+ parentXPath + ", $children:=(" + sbVarSibles.toString() +
		    ") return $children[last()]";
		List<Element> targetNode = queryNodes(targetPath);
		if(targetNode.isEmpty()) {
			insertAsFirst(parentXPath, node);
		} else {
			String nodeName = targetNode.get(targetNode.size()-1).getName();
			targetPath = parentXPath + "/scl:" + nodeName + "[last()]";
			insertAfter(targetPath, node);
		}
	}
	
	/**
	 * 向指定节点第一个子节点位置插入一个新节点。
	 * @param select 插入位置xpath
	 * @param node xml节点
	 */
	public void insertAsFirst(String select, Element node) {
		DOM4JNodeHelper.clearDefaultNS(node);
		insertAsFirst(select, node.asXML());
	}
	
	/**
	 * 向指定节点最后一个子节点位置插入一个新节点。
	 * @param select 插入位置xpath
	 * @param node xml节点
	 */
	public void insertAsLast(String select, Element node) {
		DOM4JNodeHelper.clearDefaultNS(node);
		insertAsLast(select, node.asXML());
	}
	
	/**
	 * 向指定节点插入一个子节点（位置随机）。
	 * @param select 插入位置xpath
	 * @param node xml节点
	 */
	public void insert(String select, Element node) {
		DOM4JNodeHelper.clearDefaultNS(node);
		insert(select, node.asXML());
	}
	
	/**
	 * 向指定节点插入一个子节点（位置随机）。
	 * @param select 插入位置xpath
	 * @param content xml节点字符串
	 */
	public void insert(String select, String content) {
		select = DOM4JNodeHelper.clearPrefix(select);
		content = StringUtil.clearGrammarChars(content);
	    String query = "let $doc := " + getDocXPath() +
			" return insert node " + content + " into $doc"+ select ; 
	    executeUpdate(query);
	}

	/**
	 * 将指定节点用新节点替换。
	 * @param select 待替换节点xpath
	 * @param node xml节点
	 */
	public void replaceNode(String select, Element node) {
		DOM4JNodeHelper.clearDefaultNS(node);
		replaceNode(select, node.asXML());
	}
	
	public void replaceNode(String select, String node) {
		select = DOM4JNodeHelper.clearPrefix(select);
		String content = StringUtil.clearGrammarChars(node);
		String query = "let $doc := " + getDocXPath() +
				" return replace node $doc" + select + " with " + content; 
		executeUpdate(query);
	}
	
	/**
	 * 将指定节点删除。
	 * @param select 待删除节点xpath
	 */
	public void deleteNodes(String select) {
		select = DOM4JNodeHelper.clearPrefix(select);
		String query = "let $doc := " + getDocXPath() +
				" return delete node $doc"+select;
		executeUpdate(query);
	}
	
	/**
	 * 保存指定节点、指定属性值
	 * @param ndXpath
	 * @param attName
	 * @param value
	 */
	public void saveAttribute(String ndXpath, String attName, String value) {
		value = StringUtil.toXMLChars(StringUtil.nullToEmpty(value)); // 处理转义符
		ndXpath = DOM4JNodeHelper.clearPrefix(ndXpath);
		String xupdate = "let $doc:=" + getDocXPath() +
			" return for $nd in $doc" + ndXpath +
			" return if(exists($nd/@" + attName + ")) then replace value of node $nd/@" + attName + " with '" + value + "'" +
			" else insert node attribute {'" + attName + "'}{'" + value + "'} into $nd";
		if (xupdate != null)
			executeUpdate(xupdate);
	}
	
	public String getAttributeValue(String select) {
		select = DOM4JNodeHelper.clearPrefix(select);
		String query = "let $doc:=" + getDocXPath() +
			" return  $doc"+ select + "/string()" ;
		
		String value = execute(query);
		if(null != value)
		  value = value.trim();
		
		return value;
	}
	
	public List<String> getAttributeValues(String select) {
		select = DOM4JNodeHelper.clearPrefix(select);
		String query = "let $doc:=" + getDocXPath()
				+ " return  $doc" + select + "/string()";
		return executeQuery(query);
	}
	
	/**
	 * 按xquery查询属性值
	 * @param xquery
	 * @return
	 */
	public List<String> getAttributeStrings(String xquery) {
		xquery = DOM4JNodeHelper.clearPrefix(xquery);
		return executeQuery(xquery);
	}
	
	/**
	 * 得到Node的值
	 */
	public String getNodeValue(String select) {
		select = DOM4JNodeHelper.clearPrefix(select);
		String value = null;
		String query = "let $doc := " + getDocXPath() +
			" return  $doc" + select + "/string()" ;
		
		value = execute(query);
		if(null != value)
		  value = value.trim();
		if (value == null && existsNode(select))
			value = "";
		
		return value;
	}
	
	/**
	 * 添加一个属性attName，值为value
	 */
	public void appendAttribute(String ndXpath, String attName, String value) {
		value = StringUtil.toXMLChars(value); // 处理转义符
		ndXpath = DOM4JNodeHelper.clearPrefix(ndXpath);
		String xupdate = "let $doc := " + getDocXPath() +
			" return insert node attribute {'" + attName + "'}{'" + value + "'} into $doc" + ndXpath;
		executeUpdate(xupdate);
	}

	/**
	 * 执行一条XQuery查询语句，将结果以List&lt;Element&gt;形式返回。
	 * @param xquery 任意XQuery查询语句
	 * @return
	 */
	public List<Element> queryNodes(String xquery) {
		xquery = DOM4JNodeHelper.clearPrefix(xquery);
		List<Element> result = new ArrayList<Element>();
		String content = execute(xquery);
		if (!StringUtil.isEmpty(content))
			result = XMLDBHelper.getNodesFromResult(content);
		return result;
	}
	
	/**
	 * 执行一条XQuery查询语句，将结果以List&lt;String&gt;形式返回。
	 * @param xquery
	 * @return
	 */
	public List<String> queryAttributes(String xquery) {
		xquery = DOM4JNodeHelper.clearPrefix(xquery);
		return executeQuery(xquery);
	}
	
	/**
	 * 执行一条XQuery查询语句，将结果以String形式返回。
	 * @param xquery
	 * @return
	 */
	public String queryAttribute(String xquery) {
		xquery = DOM4JNodeHelper.clearPrefix(xquery);
		String result = execute(xquery);
		if(null != result)
			result = result.trim();
		if (result == null && existsNode(xquery))
			result = "";
		return result;
	}
	
	private String execute(String strXq) {
		if (null == strXq || "".equals(strXq))
			return "";
		strXq = getNamespace() + strXq;
		try {
			return new XQuery(strXq).execute(context);
		} catch (BaseXException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 指定一条XQuery更新语句(命名空间前缀为scl)。
	 * 
	 * @param xupdate
	 * @return
	 */
	@Override
	public int executeUpdate(String xupdate) {
		execute(xupdate);
		return 1;
	}
	
	/**
	 * 指定一条XQuery查询语句。
	 * @param xquery
	 * @return
	 * @throws DriverException
	 */
	private synchronized List<String> executeQuery(String xquery) {
		List<String> result = new ArrayList<String>();
		String content = execute(xquery);
		if (!StringUtil.isEmpty(content)) {
			String[] temp = content.split("\r\n");
			result.addAll(java.util.Arrays.asList(temp));
		}
		return result;
		
	}

	@Override
	public void update(String select, String value) {
		select = DOM4JNodeHelper.clearPrefix(select);
		String query = "let $doc := " + getDocXPath() +
			" return replace value of node $doc" + select + " with '" + value + "'";
		executeUpdate(query);
	}


	@Override
	public void insertAfter(String select, String content) {
		select = DOM4JNodeHelper.clearPrefix(select);
		content = StringUtil.clearGrammarChars(content);
		String query = "let $doc := " + getDocXPath() +
			" return insert node " + content + " after $doc" + select;
	    executeUpdate(query);
	}

	@Override
	public void insertAsFirst(String select, String content) {
		content = StringUtil.clearGrammarChars(content);
		select = DOM4JNodeHelper.clearPrefix(select);
		String parentPath = "let $doc := " + getDocXPath() ; 
		String xupdate = null;
		if(countNodes(select + "/*")==0) {
			xupdate = parentPath + " return insert node " + content + " into $doc" + select;
		} else {
			xupdate = parentPath + " return insert node " + content + " as first into $doc" + select;
		}
		executeUpdate(xupdate);
	}

	@Override
	public void insertAsLast(String select, String content) {
		content = StringUtil.clearGrammarChars(content);
		select = DOM4JNodeHelper.clearPrefix(select);
		String parentPath = "let $doc := " + getDocXPath();
		String xupdate = null; 
		if(countNodes(select + "/*")==0) {
		   xupdate = parentPath + " return insert node " + content + " into $doc" + select;
		} else {
		   xupdate = parentPath + " return insert node " + content + " as last into $doc" + select;
		}
		executeUpdate(xupdate);
	}

	@Override
	public void insertBefore(String select, String content) {
		select = DOM4JNodeHelper.clearPrefix(select);
		content = StringUtil.clearGrammarChars(content);
		String query = "let $doc := " + getDocXPath() +
			" return insert node " + content + " before $doc" + select;
		executeUpdate(query);
	}

	@Override
	public int countNodes(String select) {
		int count = 0;
		select = DOM4JNodeHelper.clearPrefix(select);
		String query = "let $doc:=" + getDocXPath()
				+ " return count($doc" + select + ")";
		List<String> rs = executeQuery(query);
		if (rs != null && rs.size()>0) {
			String num = rs.get(0);
			count = Integer.valueOf(num);
		}
		return count;
	}

	@Override
	public synchronized void loadDocFromString(String docName, String xmlStr) {
		String tempPath = Constants.homeDir + docName + System.currentTimeMillis() + ".temp"; //$NON-NLS-1$
		File tempFile = new File(tempPath);
		//为避免文件字符导致加载失败，先将数据存至临时文件然后加载。
		XMLFileManager.saveUTF8File(xmlStr, tempFile);
		loadDocument(docName, tempPath);
		tempFile.deleteOnExit();
	}
	
	@Override
	public synchronized void loadElementAsDocument(String docName, Element node) {
		loadDocFromString(docName, node.asXML());
	}

	@Override
	public void moveDown(String select, int curRow) {
		moveTo(select, curRow, curRow + 1);
	}

	@Override
	public void moveUp(String select, int curRow) {
		moveTo(select, curRow, curRow - 1);
	}
	
	@Override
	public void moveTo(String select, int curRow, int newRow) {
		if (curRow == newRow)
			return;
		select = DOM4JNodeHelper.clearPrefix(select);
		String xqDelete = select + "[" + curRow + "]"; 
		String xqInsert = select + "[" + (newRow - 1) + "]";
		try {
			Element curNode = selectSingleNode(xqDelete);
			if(null == curNode)
				return;
			deleteNodes(xqDelete);
			if (newRow == 1)
				insertBefore(select + "[1]", curNode);
			else
				insertAfter(xqInsert, curNode);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 	
	}

	@Override
	public void insertNodeByDoc(Element node, String docName, String select) {
		select = DOM4JNodeHelper.clearPrefix(select);
		loadElementAsDocument(docName, node);
		String xupdate = "let $doc :=" + getDocXPath() +
				 " return insert node (db:open('" + getProject() + "','" + docName + "'))/*[1]" +
				 " after $doc" + select;
		executeUpdate(xupdate);
	}

	@Override
	public void replaceNodeByDoc(Element node, String docName, String select) {
		select = DOM4JNodeHelper.clearPrefix(select);
		loadElementAsDocument(docName, node);
		String xupdate = "let $doc:=" + getDocXPath()
			    + " return replace node $doc" + select 
				+ " with (db:open('" + getProject() + "','" + docName + "'))/*[1]";
		executeUpdate(xupdate);	
	}

	@Override
	public void setAutoCommit(boolean autoCommit) {
	}

	@Override
	public boolean isAutoCommit() {
		return true;
	}
	
	@Override
	public void forceBegin() {
	}

	@Override
	public void forceCommit() {		
	}

	@Override
	public void forceRollback() {
	}

	@Override
	public String getDocXPath() {
		return "db:open('" + getProject() + "','"
			+ getDocument() + "')";
	}

	@Override
	public void close() {
		if (context != null) {
			context.close();
		}
	}
	
	private void release() {
		close();
		XMLDBAdmin.stopDB();
	}
	
	private void recover() {
		context = new Context();
	}
	
}
