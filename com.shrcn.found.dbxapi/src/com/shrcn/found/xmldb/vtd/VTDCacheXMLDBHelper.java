/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.AbstractDBBroker;
import com.shrcn.found.xmldb.IFunction;

/**
 * 由于VTD接口对某一offset的节点修改后必须重新加载，因此所有的xml修改处理都要先在缓存中进行，
 * 等到保存时统一合并以提高修改效率，避免出错。
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-9-30
 */
public class VTDCacheXMLDBHelper extends AbstractDBBroker {

	private ScdModifier sm = new ScdModifier();
	private VTDXMLDBHelper vtd = new VTDXMLDBHelper();
	
	public synchronized void loadDocument(String docName, String filePath) {
		vtd.loadDocument(docName, filePath);
		sm.reset();
	}

	@Override
	public synchronized void exportFormatedDoc(String projectName,
			String docName, String outPath) {
		syncModify();
		vtd.exportFormatedDoc(projectName, docName, outPath);
		loadDocument(docName, outPath);
	}

	@Override
	public synchronized void exportFormatedDoc(String outPath) {
		exportFormatedDoc(null, null, outPath);
	}
	
	/**
	 * 这里的复制操作是为了保证原始数据不被修改。 
	 * @param nodes
	 * @return
	 */
	private List<Element> createCopy(List<Element> nodes) {
		List<Element> copys = new ArrayList<>();
		boolean copyed = false;
		for (Object o : nodes) {
			if (o instanceof Element) {
				copys.add(((Element)o).createCopy());
				copyed = true;
			}
		}
		return copyed ? copys : nodes;
	}
	
	@Override
	public synchronized List<Element> selectNodes(String select) {
		select = DOM4JNodeHelper.clearPrefix(select);
		String subXpath = VTDXPathUtil.getSubXPath(select);
		if (VTDXPathUtil.isPartRoot(select)) {	// 所有缓存中查询
			List<Element> result = new ArrayList<Element>();
			List<Element> cacheParts = sm.getCacheNode(select);
			String partXPath = VTDXPathUtil.getPartRootXPath(select);
			String partName = partXPath.substring(partXPath.lastIndexOf('/') + 1);
			for (Element cachePart : cacheParts) {
				String ndName = cachePart.getName();
				if (".".equals(subXpath) && ndName.equals(partName)) {
					result.add(cachePart.createCopy());
				} else {
					result.addAll(createCopy(DOM4JNodeHelper.selectNodes(cachePart, subXpath)));
				}
			}
			List<String> modifys = sm.getUpdatedNodes();
			List<String> dels = sm.getRemovedNodes();
			List<Element> nodesInDoc = vtd.selectNodesOnlyAtts(VTDXPathUtil.getPartRootXPath(select), partName);
			for (Element nodeInDoc : nodesInDoc) {
				String name = nodeInDoc.attributeValue("name");
				String modifyXpath = partXPath;
				if (!StringUtil.isEmpty(name)) {
					modifyXpath = modifyXpath + "[@name='" + name + "']";
				}
				if (!modifys.contains(modifyXpath) && !dels.contains(modifyXpath)) {
					Element nodeEl = vtd.selectSingleNode(modifyXpath);
					result.addAll(createCopy(DOM4JNodeHelper.selectNodes(nodeEl, subXpath)));
				}
			}
			return result;
		} else {								// 单个缓存中查询
			if (sm.isInCache(select)) {
				Element cacheNode = sm.getCacheNode(select).get(0);
				return createCopy(DOM4JNodeHelper.selectNodes(cacheNode, subXpath));
			} else {
				return vtd.selectNodes(select);
			}
		}
	}

	@Override
	public synchronized List<Element> selectNodesOnlyAtts(String select, String ndsName) {
		select = DOM4JNodeHelper.clearPrefix(select);
		if (VTDXPathUtil.isPartRoot(select)) {	// 所有缓存中查询
			List<Element> result = new ArrayList<Element>();
			List<Element> cacheParts = sm.getCacheNode(select);
			String subXpath = VTDXPathUtil.getSubXPath(select);
			String partXPath = VTDXPathUtil.getPartRootXPath(select);
			String partName = partXPath.substring(partXPath.lastIndexOf('/') + 1);
			for (Element cachePart : cacheParts) {
				if (".".equals(subXpath)) {
					Element nodeTemp = DOM4JNodeHelper.createSCLNode(ndsName);
					DOM4JNodeHelper.copyAttributes(cachePart, nodeTemp);
					result.add(nodeTemp);
				} else {
					result.addAll(DOM4JNodeHelper.selectNodesOnlyAtts(cachePart.createCopy(), subXpath, ndsName));
				}
			}
			List<String> modifys = sm.getUpdatedNodes();
			List<String> dels = sm.getRemovedNodes();
			if (".".equals(subXpath)) { // 非子节点查询
				List<Element> nodesInDoc = vtd.selectNodesOnlyAtts(VTDXPathUtil.getPartRootXPath(select), partName);
				for (Element nodeInDoc : nodesInDoc) {
					String name = nodeInDoc.attributeValue("name");
					if (!StringUtil.isEmpty(name)) {
						String modifyXpath = partXPath + "[@name='" + name + "']";
						if (!modifys.contains(modifyXpath) && !dels.contains(modifyXpath)) {
							result.add(nodeInDoc);
						}
					} else {
						result.add(nodeInDoc);
					}
				}
			} else {
				List<Element> nodesInDoc = vtd.selectNodesOnlyAtts(VTDXPathUtil.getPartRootXPath(select), partName);
				for (Element nodeInDoc : nodesInDoc) {
					String name = nodeInDoc.attributeValue("name");
					if (!StringUtil.isEmpty(name)) {
						String modifyXpath = partXPath + "[@name='" + name + "']";
						if (!modifys.contains(modifyXpath) && !dels.contains(modifyXpath)) {
							result.addAll(vtd.selectNodesOnlyAtts(modifyXpath + subXpath.substring(1), ndsName));
						}
					} else {
						result.addAll(vtd.selectNodesOnlyAtts(partXPath + subXpath.substring(1), ndsName));
					}
				}
			}
			return result;
		} else {								// 单个缓存中查询
			if (sm.isInCache(select)) {
				Element cacheNode = sm.getCacheNode(select).get(0);
				return DOM4JNodeHelper.selectNodesOnlyAtts(cacheNode.createCopy(), VTDXPathUtil.getSubXPath(select), ndsName); // 复制节点是为保证子节点不被删
			} else {
				return vtd.selectNodes(select);
			}
		}
	}

	@Override
	public synchronized List<Element> selectNodes(String select, int offset, int number) {
		List<Element> resultAll = selectNodes(select);
		return resultAll.subList(offset, offset + number);
	}

	@Override
	public synchronized Element selectSingleNode(String select) {
		List<Element> result = selectNodes(select);
		return (result!=null && result.size()>0) ? result.get(0) : null;
	}

	@Override
	public synchronized boolean existsNode(String select) {
		return countNodes(select) > 0;
	}

	@Override
	public synchronized int countNodes(String select) {
		List<Element> result = selectNodes(select);
		return (result!=null) ? result.size() : 0;
	}

	private void resolvCacheNode(String select) {
		String xpath = VTDXPathUtil.getCacheXPath(select);
		String partXpath = VTDXPathUtil.getPartRootXPath(select);
		String partName = partXpath.substring(partXpath.lastIndexOf('/') + 1);
		List<Element> nds = vtd.selectNodesOnlyAtts(xpath, partName);
		List<String> dels = sm.getRemovedNodes();
		for (Element nd : nds) {
			String ndXpath = partXpath;
			if (!StringUtil.isEmpty(nd.attributeValue("name"))) {
				ndXpath += "[@name='" + nd.attributeValue("name") + "']";
			}
			if (!dels.contains(ndXpath) && !isInCache(ndXpath)) {
				sm.addCacheNode(ndXpath, vtd.selectSingleNode(ndXpath));
			}
		}
	}

	@Override
	public synchronized void insertBefore(String select, Element node) {
		if (!VTDXPathUtil.isCachePart(select) && !sm.isInCache(select)) { 	// 对缓存操作
			resolvCacheNode(select);
		}
		sm.insertBefore(select, node);
	}

	@Override
	public synchronized void insertAfter(String select, Element node) {
		if (!VTDXPathUtil.isCachePart(select) && !sm.isInCache(select)) { 	// 对缓存操作
			resolvCacheNode(select);
		}
		sm.insertAfter(select, node);
	}

	@Override
	public synchronized void insertAfterType(String parentXPath, String[] types, Element node) {
		if (!VTDXPathUtil.isCachePart(parentXPath) && !sm.isInCache(parentXPath)) { 	// 对缓存操作
			resolvCacheNode(parentXPath);
		}
		sm.insertAfterType(parentXPath, types, node);
	}

	@Override
	public synchronized void insertAfterType(String parentXPath, String[] types, String node) {
		insertAfterType(parentXPath, types, DOM4JNodeHelper.parseText2Node(node));
	}
	
	@Override
	public void insertAfterType(String parentXPath, String[] types, List<Element> nodes) {
		if (!VTDXPathUtil.isCachePart(parentXPath) && !sm.isInCache(parentXPath)) { 	// 对缓存操作
			resolvCacheNode(parentXPath);
		}
		sm.insertAfterType(parentXPath, types, nodes);
	}

	@Override
	public synchronized void insertAsFirst(String select, Element node) {
		if (!VTDXPathUtil.isRoot(select) && !sm.isInCache(select)) { 	// 对缓存操作
			resolvCacheNode(select);
		}
		sm.insertAsFirst(select, node);
	}

	@Override
	public synchronized void insertAsLast(String select, Element node) {
		if (!VTDXPathUtil.isRoot(select) && !sm.isInCache(select)) { 	// 对缓存操作
			resolvCacheNode(select);
		}
		sm.insertAsLast(select, node);
	}

	@Override
	public synchronized void insert(String select, Element node) {
		if (!VTDXPathUtil.isRoot(select) && !sm.isInCache(select)) { 	// 对缓存操作
			resolvCacheNode(select);
		}
		sm.insert(select, node);
	}

	@Override
	public synchronized void moveDown(String select, int curRow) {
		moveTo(select, curRow, curRow + 1);
	}

	@Override
	public synchronized void moveUp(String select, int curRow) {
		moveTo(select, curRow, curRow - 1);
	}

	@Override
	public synchronized void moveTo(String select, int curRow, int newRow) {
		if (!VTDXPathUtil.isCachePart(select) && !sm.isInCache(select)) { 	// 对缓存操作
			resolvCacheNode(select);
		}
		sm.moveTo(select, curRow, newRow);
	}

	@Override
	public synchronized void replaceNode(String select, Element node) {
		if (!VTDXPathUtil.isCachePart(select) && !sm.isInCache(select)) { 	// 对缓存操作
			resolvCacheNode(select);
		}
		sm.replaceNode(select, node);
	}

	@Override
	public synchronized void deleteNodes(String select) {
		if (!VTDXPathUtil.isCachePart(select) && !sm.isInCache(select)) { 	// 对缓存操作
			resolvCacheNode(select);
		}
		sm.deleteNodes(select);
	}

	@Override
	public synchronized void saveAttribute(String select, String attName,
			String value) {
		if (!sm.isInCache(select)) { 	// 对缓存操作
			resolvCacheNode(select);
		}
		sm.saveAttribute(select, attName, value);
	}

	@Override
	public synchronized void appendAttribute(String select, String attName,
			String value) {
		saveAttribute(select, attName, value);
	}

	@Override
	public synchronized void update(String select, String value) {
		if (!sm.isInCache(select)) { 	// 对缓存操作
			resolvCacheNode(select);
		}
		sm.update(select, value);
	}
	
	@Override
	public synchronized void updateOnce(String select, String value) {
		if (sm.isInCache(select)) { 	// 对缓存操作
			sm.update(select, value);
		} else {
			vtd.update(select, value);
		}
	}

	@Override
	public synchronized void forceCommit() {
		syncModify();
		vtd.forceCommit();
	}

	private void syncModify() {
		if (!sm.hasModified())
			return;
		List<String> dels = sm.getRemovedNodes();
		List<String> modifys = sm.getUpdatedNodes();
		for (String string : dels) {
			vtd.deleteNodes(string);
		}
		for (String key : modifys) {
			Element node = vtd.selectSingleNode(key);
			Element cacheNode = sm.getCacheNode(key).get(0);
			String temp = DOM4JNodeHelper.asXML(cacheNode);
			if (node == null) {		// 不存在
				// <Private>,<Header>,<Substation>,<Communication>,<DataTypeTemplates>
				if (key.indexOf("/Private")>0) {
					vtd.insertAsFirst("/SCL", temp);
				} else if (key.indexOf("/Header")>0) {
					vtd.insertAfterType("/SCL", new String[] {"Private"}, temp);
				} else if (key.indexOf("/Substation")>0) {
					vtd.insertAfterType("/SCL", new String[] {"Private", "Header"}, temp);
				} else if (key.indexOf("/Communication")>0) {
					vtd.insertAfterType("/SCL", new String[] {"Private", "Header", "Substation"}, temp);
				} else if (key.indexOf("/IED")>0) {
					vtd.insertAfter("/SCL/Communication", temp);
				} else if (key.indexOf("/DataTypeTemplates")>0) {
					vtd.insertAsLast("/SCL", temp);
				}
			} else {				 // 存在
				vtd.replaceNode(key, temp);
			}
		}
		sm.reset();
	}

	@Override
	public String getAttributeValue(String select) {
		String subXPath = VTDXPathUtil.getSubXPath(select);
		if (VTDXPathUtil.isPartRoot(select)) {
			List<Element> parts = selectNodes(select);
			if (parts!=null && parts.size()>0) {
				Node att = parts.get(0);
				return (null != att) ? ((Attribute) att).getValue() : null;
			} else {
				return null;
			}
		} else {
			if (sm.isInCache(select)) {
				Element cacheNode = sm.getCacheNode(select).get(0);
				return DOM4JNodeHelper.getAttributeValue(cacheNode, subXPath);
			} else {
				return vtd.getAttributeValue(select);
			}
		}
	}

	@Override
	public List<String> getAttributeValues(String select) {
		String subXPath = VTDXPathUtil.getSubXPath(select);
		if (VTDXPathUtil.isPartRoot(select)) {
			List parts = selectNodes(select);
			List<String> values = new ArrayList<>();
			for (Object o : parts) {
				Node part = (Node) o;
				values.add(((Attribute) part).getValue());
			}
			return values;
		} else {
			if (sm.isInCache(select)) {
				Element cacheNode = sm.getCacheNode(select).get(0);
				return DOM4JNodeHelper.getAttributeValues(cacheNode, subXPath);
			} else {
				return vtd.getAttributeValues(select);
			}
		}
	}

	@Override
	public String getNodeValue(String select) {
		String subXPath = VTDXPathUtil.getSubXPath(select);
		if (VTDXPathUtil.isPartRoot(select)) {
			List<Element> parts = selectNodes(select);
			if (parts!=null && parts.size()>0) {
				return parts.get(0).getText();
			} else {
				return null;
			}
		} else {
			if (sm.isInCache(select)) {
				Element cacheNode = sm.getCacheNode(select).get(0);
				return DOM4JNodeHelper.getNodeValue(cacheNode, subXPath);
			} else {
				return vtd.getNodeValue(select);
			}
		}
	}

	@Override
	public void close() {
		vtd.close();
	}

	@Override
	public String getDocXPath() {
		return null;
	}

	@Override
	public boolean existsDocument(String docName) {
		return false;
	}

	@Override
	public void loadElementAsDocument(String docName, Element node) {
		vtd.loadElementAsDocument(docName, node);
		sm.reset();
	}

	@Override
	public void loadDocFromString(String docName, String xmlStr) {
		vtd.loadDocFromString(docName, xmlStr);
		sm.reset();
	}

	@Override
	public void removeDocument(String docName, String colName) {
	}
	
	@Override
	public IFunction createFunction(Class<?> funClass,
			Map<String, Object> params) {
		return vtd.createFunction(funClass, params);
	}
	
	@Override
	public IFunction createUpdateFunction(Class<?> funClass,
			Map<String, Object> params) {
		return vtd.createUpdateFunction(funClass, params);
	}
	
	@Override
	public boolean isInCache(String select) {
		return sm.isInCache(select);
	}

	@Override
	public void insertBefore(String select, String content) {
		Element node = DOM4JNodeHelper.parseText2Node(content);
		insertBefore(select, node);
	}

	@Override
	public void insertAfter(String select, String content) {
		Element node = DOM4JNodeHelper.parseText2Node(content);
		insertAfter(select, node);
	}

	@Override
	public void insertAsFirst(String select, String content) {
		Element node = DOM4JNodeHelper.parseText2Node(content);
		insertAsFirst(select, node);
	}

	@Override
	public void insertAsLast(String select, String content) {
		Element node = DOM4JNodeHelper.parseText2Node(content);
		insertAsLast(select, node);
	}

	@Override
	public void insert(String select, String content) {
		Element node = DOM4JNodeHelper.parseText2Node(content);
		insert(select, node);
	}

	@Override
	public void replaceNode(String select, String content) {
		Element node = DOM4JNodeHelper.parseText2Node(content);
		replaceNode(select, node);
	}

	@Override
	public List<String> getAttributeStrings(String xquery) {
		return null;
	}

	@Override
	public List<Element> queryNodes(String xquery) {
		return null;
	}

	@Override
	public List<String> queryAttributes(String xquery) {
		return null;
	}

	@Override
	public String queryAttribute(String xquery) {
		return null;
	}

	@Override
	public int executeUpdate(String xupdate) {
		return 0;
	}

	@Override
	public boolean isAutoCommit() {
		return false;
	}

	@Override
	public void setAutoCommit(boolean autoCommit) {
	}

	@Override
	public void forceBegin() {
	}

	@Override
	public void forceRollback() {
	}

	@Override
	public void insertNodeByDoc(Element element, String docName, String select) {
	}

	@Override
	public void replaceNodeByDoc(Element element, String docName, String select) {
	}

}
