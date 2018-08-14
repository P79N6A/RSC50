/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.das;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.VisitorSupport;

import com.shrcn.found.common.Constants;

/**
 * 。
 * @author 孙春颖
 * @version 1.0, 2015-8-6
 */
public class NamespaceFindVisitor extends VisitorSupport {

	private Map<String, Namespace> nsMap;
	
	public NamespaceFindVisitor() {
		this.nsMap = new HashMap<String, Namespace>();
	}

	public void visit(Element node) {
		Namespace ns = node.getNamespace();
		if ("".equals(ns.getPrefix())) {
			Namespace namespace = new Namespace("", Constants.uri);
			node.setQName(QName.get(node.getName(), namespace));
		} else {
			nsMap.put(ns.getURI(), ns);
		}
		ns = node.getNamespaceForURI(Constants.schema);
		if (ns != null)
			node.remove(ns);
		for (Object o : node.additionalNamespaces()) {
			ns = (Namespace) o;
			String uri = ns.getURI();
			if (!nsMap.containsKey(uri)) {
				if (uri.indexOf("www.iec.ch") < 0 && uri.indexOf("www.w3.org") < 0) {
					nsMap.put(uri, ns);
				}
			}
		}
	}

	public Namespace[] getNsList() {
		return nsMap.values().toArray(new Namespace[0]);
	}
}
