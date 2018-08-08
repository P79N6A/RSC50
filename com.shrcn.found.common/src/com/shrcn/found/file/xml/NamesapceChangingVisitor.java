/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.xml;

import java.util.ListIterator;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.VisitorSupport;

/**
 * 修改namespace的visitor类。用于解决部分icd、iid文件没有使用
 * xmlns="http://www.iec.ch/61850/2003/SCL"的问题。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-5-28
 */
/**
 * $Log: NamesapceChangingVisitor.java,v $
 * Revision 1.1  2013/03/29 09:37:44  cchun
 * Add:创建
 *
 * Revision 1.1  2010/05/31 02:53:06  cchun
 * Add:命名空间转换visitor
 *
 */
public class NamesapceChangingVisitor extends VisitorSupport {
	private Namespace from;
	private Namespace to;

	public NamesapceChangingVisitor(Namespace from, Namespace to) {
		this.from = from;
		this.to = to;
	}

	public void visit(Element node) {
		Namespace ns = node.getNamespace();

		if (ns.getURI().equals(from.getURI())) {
			QName newQName = new QName(node.getName(), to);
			node.setQName(newQName);
		}

		ListIterator<?> namespaces = node.additionalNamespaces().listIterator();
		while (namespaces.hasNext()) {
			Namespace additionalNamespace = (Namespace) namespaces.next();
			if (additionalNamespace.getURI().equals(from.getURI())) {
				namespaces.remove();
			}
		}
	}

}