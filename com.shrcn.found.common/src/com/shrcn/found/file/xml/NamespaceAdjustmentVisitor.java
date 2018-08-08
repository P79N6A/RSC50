/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.file.xml;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;
import org.dom4j.Text;
import org.dom4j.Visitor;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-8-5
 */
/*
 * 修改历史
 * $Log: NamespaceAdjustmentVisitor.java,v $
 * Revision 1.1  2013/03/29 09:37:44  cchun
 * Add:创建
 *
 * Revision 1.2  2009/08/18 09:36:12  cchun
 * Update:合并代码
 *
 * Revision 1.1.2.3  2009/08/05 07:38:15  cchun
 * Update:调整格式
 *
 * Revision 1.1.2.2  2009/08/05 07:37:43  cchun
 * Update:添加namespace visitor
 *
 */
public class NamespaceAdjustmentVisitor implements Visitor {

	private Namespace namespace;

	public NamespaceAdjustmentVisitor(Namespace namespace) {
	this.namespace = namespace;
	}

	public void visit(Element e) {
		e.remove(e.getNamespace());
		e.setQName(QName.get(e.getName(), namespace));
	}

	public void visit(Document arg0) {}
	public void visit(DocumentType arg0) {}
	public void visit(Attribute arg0) {}
	public void visit(CDATA arg0) {}
	public void visit(Comment arg0) {}
	public void visit(Entity arg0) {}
	public void visit(Namespace arg0) {}
	public void visit(ProcessingInstruction arg0) {}
	public void visit(Text arg0) {}
}

