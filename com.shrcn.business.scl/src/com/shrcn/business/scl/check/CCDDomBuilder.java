/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.check;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;

import com.shrcn.found.common.util.ListUtil;
import com.shrcn.found.common.util.StringUtil;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-24
 */
public class CCDDomBuilder {
	
	@SuppressWarnings("unchecked")
	public static String getCCDDocument(Document doc) {
		Element root = doc.getRootElement();
		StringBuilder txt = new StringBuilder(1024 * 5);
		txt.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		handle(root, txt);
		return txt.toString();
	}

	@SuppressWarnings("unchecked")
	private static void handle(Element parent, StringBuilder txt) {
		String sp = getSpace(parent);
		String nodeName = parent.getName();
		Namespace ns = parent.getNamespace();
		if (ns != null && !StringUtil.isEmpty(ns.getPrefix())) {
			nodeName = ns.getPrefix() + ":" + nodeName;
		}
		txt.append(sp + "<" + nodeName);
		Map<String, String> attMap = CCDTextBuilder.getAttMap(parent);
		List<String> atts = new ArrayList<>();
		atts.addAll(attMap.keySet());
		ListUtil.sortStr(atts);
		for (String att : atts) {
			txt.append(" " + att).append("=\"").append(attMap.get(att)).append("\"");
		}
		// 处理子节点
		String value = StringUtil.toXMLChars(parent.getTextTrim());
		List<Element> children = parent.elements();
		if (StringUtil.isEmpty(value) && children.size() < 1) {
			txt.append("/>\n");
		} else {
			boolean hasText = !StringUtil.isEmpty(value);
			txt.append(">" + (hasText?"":"\n"));
			if (hasText) {
				txt.append(value);
			} else if (children.size() > 0) {
				for (Element child : children) {
					handle(child, txt);
				}
			}
			txt.append((hasText?"":sp) + "</" + nodeName + ">\n");
		}
	}
	
	private static String getSpace(Element nd) {
		String sp = "";
		Element parent = nd.getParent();
		while (parent != null) {
			sp += "  ";
			parent = parent.getParent();
		}
		return sp;
	}
}

