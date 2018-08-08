/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.xml;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.VisitorSupport;

import com.shrcn.found.common.util.StringUtil;

/**
 * 。
 * @author 孙春颖
 * @version 1.0, 2015-8-6
 */
public class NamespaceClearVisitor extends VisitorSupport {

	public NamespaceClearVisitor() {
	}

	public void visit(Element node) {
		Namespace ns = node.getNamespace();
		if ("".equals(ns.getPrefix()) && !StringUtil.isEmpty(ns.getURI())) {
			Namespace namespace = null;
			node.setQName(QName.get(node.getName(), namespace));
		}
		for (Object o : node.additionalNamespaces()) {
			node.remove((Namespace) o);
		}
	}
}
