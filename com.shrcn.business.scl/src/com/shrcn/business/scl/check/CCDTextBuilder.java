/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;

import com.shrcn.found.common.util.ListUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-24
 */
public class CCDTextBuilder {
	
	@SuppressWarnings("unchecked")
	public static String getCCDText(Document doc) {
		Element root = doc.getRootElement().createCopy();
		StringBuilder txt = new StringBuilder(1024 * 5);
		txt.append("<?xmlversion=\"1.0\"encoding=\"UTF-8\"?>");
		// 清理<SUB/FCDA>中除bType之外的属性
		List<Element> allFcda = new ArrayList<>();
		for (Object o : root.elements()) {
			Element sub = (Element) o;
			if (sub.getName().endsWith("SUB")) {
				for (Object cbo : sub.elements()) {
					Element cb = (Element) cbo;
					for (Object dso : cb.elements("DataSet")) {
						Element ds = (Element) dso;
						allFcda.addAll(ds.elements("FCDA"));
					}
				}
			}
		}
		for (Element fcda : allFcda) {
			List<String> attsDel = new ArrayList<>();
			List<Attribute> atts = fcda.attributes();
			for (Attribute att : atts) {
				String attname = att.getName();
				if (!"bType".equals(attname)) {
					attsDel.add(attname);
				}
			}
			DOM4JNodeHelper.clearAttributes(fcda, attsDel.toArray(new String[5]));
		}
		handle(root, txt);
		
		return txt.toString();
	}

	@SuppressWarnings("unchecked")
	private static void handle(Element parent, StringBuilder txt) {
		String nodeName = parent.getName();
		Namespace ns = parent.getNamespace();
		if (ns != null && !StringUtil.isEmpty(ns.getPrefix())) {
			nodeName = ns.getPrefix() + ":" + nodeName;
		}
		txt.append("<" + nodeName);
		// 按字母序处理属性，属性中空格需保留
		if ("IED".equals(parent.getName())) {
			txt.append("name=\"" + parent.attributeValue("name") + "\"");
		} else {
			Map<String, String> attMap = getAttMap(parent);
			List<String> atts = new ArrayList<>();
			atts.addAll(attMap.keySet());
			ListUtil.sortStr(atts);
			for (String att : atts) {
				if (!"desc".equals(att)) {
					txt.append(att).append("=\"").append(attMap.get(att)).append("\"");
				}
			}
		}
		// 处理子节点
		String value = parent.getTextTrim();
		List<Element> children = parent.elements();
		if (StringUtil.isEmpty(value) && children.size() < 1) {
			txt.append("/>");
		} else {
			txt.append(">");
			if (!StringUtil.isEmpty(value)) {
				txt.append(value);
			} else if (children.size() > 0) {
				for (Element child : children) {
					handle(child, txt);
				}
			}
			txt.append("</" + nodeName + ">");
		}
	}
	
	/**
	 * 得到属性和命名空间映射表（自动补充的命名空间排除在外）。
	 * @param node
	 * @return
	 */
	public static Map<String, String> getAttMap(Element node) {
		Namespace ns = node.getNamespace();
		List<Attribute> atts = node.attributes();
		Map<String, String> attMap = new HashMap<>();
		for (Attribute att : atts) {
			String attName = att.getName();
			String attValue = StringUtil.toXMLChars(att.getValue());
			attMap.put(attName, attValue);
		}
		if (ns != null && !StringUtil.isEmpty(ns.getPrefix())) {
			if (!isLostNs(node))
				attMap.put("xmlns:" + ns.getPrefix(), ns.getURI());
		}
		for (Object o : node.additionalNamespaces()) {
			if (!isLostNs(node)) {
				ns = (Namespace) o;
				attMap.put("xmlns:" + ns.getPrefix(), ns.getURI());
			}
		}
		return attMap;
	}
	
	/**
	 * 判断命名空间是否为查询方法自动补上去的，
	 * 若是返回true，否则返回false。
	 * @param node
	 * @return
	 */
	private static boolean isLostNs(Element node) {
		Namespace ns = node.getNamespace();
		if (ns == null)
			return false;
		String prefix = ns.getPrefix();
		String parentsName = node.getName();
		Element parent = node.getParent();
		if (parent!=null) {
			parentsName = parent.getName() + "/" + parentsName;
			parent = parent.getParent();
			if (parent!=null) {
				parentsName = parent.getName() + "/" + parentsName;
			}
		}
		return XMLDBHelper.lostNsList!=null && XMLDBHelper.lostNsList.contains(prefix + "_" + parentsName);
	}
}

