/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.xml;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.VisitorSupport;

import com.shrcn.found.common.util.StringUtil;

/**
 * 。
 * @author 孙春颖
 * @version 1.0, 2015-8-6
 */
public class NamespaceUnifyVisitor extends VisitorSupport {

	public NamespaceUnifyVisitor() {
	}

	public void visit(Element node) {
		Element root = node.getDocument().getRootElement();
		Namespace ns = node.getNamespace();
		if (ns != null && !StringUtil.isEmpty(ns.getPrefix()))
			root.add(ns);
	}
}
