/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.shrcn.found.common.cache.CacheFactory;
import com.shrcn.found.common.cache.CacheWrapper;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-10-10
 */
public class ScdModifier {
	
	private static final String cacheName = "modifyCache";
	
	private CacheWrapper xmCache;

	private List<String> updatedNodes = new ArrayList<String>();
	private List<String> removedNodes = new ArrayList<String>();
	
	public ScdModifier() {
	}

	public CacheWrapper getXmCache() {
		if (xmCache == null) {
			CacheFactory.createHashMapWrapper(cacheName); 
			this.xmCache = CacheFactory.getCacheWrapper(cacheName);
		}
		return xmCache;
	}
	
	public void addCacheNode(String xpath, Element nd) {
		if (nd == null)
			return;
		xpath = DOM4JNodeHelper.clearPrefix(xpath);
		int p = removedNodes.indexOf(xpath);
		if (p > -1)
			removedNodes.remove(p);
		getXmCache().put(xpath, nd);
		if (!updatedNodes.contains(xpath)) {
			updatedNodes.add(xpath);
		}
	}
	
	private void removeCacheNode(String xpath) {
		xpath = DOM4JNodeHelper.clearPrefix(xpath);
		if (updatedNodes.contains(xpath)) {
			getXmCache().remove(xpath);
			updatedNodes.remove(xpath);
		}
		removedNodes.add(xpath);
	}
	
	public List<Element> getCacheNode(String select) {
		List<Element> nds = new ArrayList<>();
		String xpath = VTDXPathUtil.getCacheXPath(select);
		if (xpath != null) {
			String partRoot = VTDXPathUtil.getPartRootXPath(select);
			if (xpath.equals(partRoot)) {	// 多选
				for (String update : updatedNodes) {
					if (update.startsWith(partRoot)) {
						nds.add((Element) getXmCache().get(update));
					}
				}
			} else {						// 单选
				if (updatedNodes.contains(xpath)) {
					nds.add((Element) getXmCache().get(xpath));
				}
			}
		}
		return nds;
	}
	
	public boolean isInCache(String select) {
		return updatedNodes.contains(VTDXPathUtil.getCacheXPath(select));
	}
	
	// <Header>,<Substation>,<Communication>,<IED>,<DataTypeTemplates>
	private void cachePart(Element nd) {
		String ndName = nd.getName();
		String name = nd.attributeValue("name");
		String xpath = "/SCL/" + ndName;
		if (!StringUtil.isEmpty(name)) {
			xpath = xpath + "[@name='" + name + "']";
		}
		addCacheNode(xpath, nd);
	}
	
	public synchronized void insertBefore(String select, Element nd) {
		if (VTDXPathUtil.isCachePart(select)) {	// 缓存节点
			cachePart(nd);
		} else {								// 缓存节点子节点
			List<Element> cacheNodes = getCacheNode(select);
			if (cacheNodes!=null && cacheNodes.size()==1) {
				Element root = cacheNodes.get(0);
				if (root != null) {
					DOM4JNodeHelper.insertBefore(root, VTDXPathUtil.getSubXPath(select), nd);
				}
			}
		}
	}

	public synchronized void insertAfter(String select, Element nd) {
		if (VTDXPathUtil.isCachePart(select)) {	// 缓存节点
			cachePart(nd);
		} else {								// 缓存节点子节点
			List<Element> cacheNodes = getCacheNode(select);
			if (cacheNodes!=null && cacheNodes.size()==1) {
				Element root = cacheNodes.get(0);
				if (root != null) {
					DOM4JNodeHelper.insertAfter(root, VTDXPathUtil.getSubXPath(select), nd);
				}
			}
		}
	}

	public synchronized void insertAsFirst(String select, Element nd) {
		if (VTDXPathUtil.isRoot(select)) {	// 根节点
			cachePart(nd);
		} else {				// 缓存节点及子节点
			List<Element> cacheNodes = getCacheNode(select);
			if (cacheNodes!=null && cacheNodes.size()==1) {
				Element root = cacheNodes.get(0);
				if (root != null) {
					DOM4JNodeHelper.insertAsFirst(root, VTDXPathUtil.getSubXPath(select), nd);
				}
			}
		}
	}

	public synchronized void insertAsLast(String select, Element nd) {
		if (VTDXPathUtil.isRoot(select)) {	// 根节点
			cachePart(nd);
		} else {				// 缓存节点及子节点
			List<Element> cacheNodes = getCacheNode(select);
			if (cacheNodes!=null && cacheNodes.size()==1) {
				Element root = cacheNodes.get(0);
				if (root != null) {
					DOM4JNodeHelper.insertAsLast(root, VTDXPathUtil.getSubXPath(select), nd);
				}
			}
		}
	}

	public void insert(String select, Element node) {
		insertAsLast(select, node);
	}

	public synchronized void replaceNode(String select, Element nd) {
		if (VTDXPathUtil.isCachePart(select)) {	// 缓存节点
			cachePart(nd);
		} else {					// 缓存节点子节点
			List<Element> cacheNodes = getCacheNode(select);
			if (cacheNodes!=null && cacheNodes.size()==1) {
				Element root = cacheNodes.get(0);
				if (root != null) {
					DOM4JNodeHelper.replaceNode(root, VTDXPathUtil.getSubXPath(select), nd);
				}
			}
		}
	}

	public synchronized void deleteNodes(String select) {
		select = DOM4JNodeHelper.clearPrefix(select);
		if (VTDXPathUtil.isCachePart(select)) {
			removeCacheNode(select);
		} else {
			List<Element> cacheNodes = getCacheNode(select);
			if (cacheNodes!=null && cacheNodes.size()==1) {
				Element root = cacheNodes.get(0);
				if (root != null) {
					DOM4JNodeHelper.deleteNodes(root, VTDXPathUtil.getSubXPath(select));
				}
			}
		}
	}
	
	public void insertAfterType(String parentXPath, String[] types, Element node) {
		List<Element> nodes = new ArrayList<>();
		nodes.add(node);
		insertAfterType(parentXPath, types, nodes);
	}
	
	private Element getSingleCacheNode(String xpath) {
		List<Element> cacheNodes = getCacheNode(xpath);
		if (cacheNodes!=null && cacheNodes.size()==1)
			return cacheNodes.get(0);
		return null;
	}
	
	public synchronized void insertAfterType(String parentXPath, String[] types, List<Element> nodes) {
		boolean success = false;
		if (types != null && types.length > 0) {
			Element root = getSingleCacheNode(parentXPath);
			if (root == null)
				return;
			String subXpath = VTDXPathUtil.getSubXPath(parentXPath);
			for (int i = types.length - 1; i > -1; i--) {
				String path = subXpath + "/" + types[i] + "[last()]";
				Element ele = DOM4JNodeHelper.selectSingleNode(root, path);
				if (ele != null) {
					List children = ele.getParent().elements();
					int index = children.indexOf(ele);
					for (Element node : nodes) {
						if (index < children.size()-1)
							children.add(index + 1, node.detach());
						else
							children.add(node.detach());
						index++;
					}
					success = true;
					break;
				}
			}
		}
		if (!success) {
			for (Element node : nodes) {
				insertAsLast(parentXPath, node);
			}
		}
	}
	
	public void moveTo(String select, int curRow, int newRow) {
		if (curRow == newRow)
			return;
		Element root = getSingleCacheNode(select);
		if (root == null)
			return;
		select = DOM4JNodeHelper.clearPrefix(select);
		String xqDelete = VTDXPathUtil.getSubXPath(select) + "[" + curRow + "]";
		String xqInsert = VTDXPathUtil.getSubXPath(select) + "[" + (newRow - 1) + "]";
		try {
			Element curNode = DOM4JNodeHelper.selectSingleNode(root, xqDelete);
			if (null == curNode)
				return;
			DOM4JNodeHelper.deleteNodes(root, xqDelete);
			if (newRow == 1)
				DOM4JNodeHelper.insertBefore(root, VTDXPathUtil.getSubXPath(select) + "[1]", curNode);
			else
				DOM4JNodeHelper.insertAfter(root, xqInsert, curNode);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized void saveAttribute(String select, String attName, String value) {
		List<Element> parts = getCacheNode(select);
		if (parts==null || parts.size()<1)
			return;
		for (Element root : parts) {
			if (VTDXPathUtil.isCachePart(select)) {
				if ("name".equals(attName)) {
					String newXPath = VTDXPathUtil.getPartRootXPath(select) + "[@name='" + value + "']";
					removeCacheNode(select);
					root.addAttribute(attName, value);
					addCacheNode(newXPath, root);
				} else {
					root.addAttribute(attName, value);
				}
			} else {
				DOM4JNodeHelper.saveAttribute(root, VTDXPathUtil.getSubXPath(select), attName, value);
			}
		}
	}

	public void update(String select, String value) {
		Element root = getSingleCacheNode(select);
		if (root != null)
			DOM4JNodeHelper.update(root, VTDXPathUtil.getSubXPath(select), value);
	}

	public boolean hasModified() {
		return updatedNodes.size()>0 || removedNodes.size()>0;
	}

	public void reset() {
		if (hasModified()) {
			getXmCache().clear();
			updatedNodes.clear();
			removedNodes.clear();
		}
	}

	public List<String> getRemovedNodes() {
		return removedNodes;
	}

	public List<String> getUpdatedNodes() {
		return updatedNodes;
	}

}
