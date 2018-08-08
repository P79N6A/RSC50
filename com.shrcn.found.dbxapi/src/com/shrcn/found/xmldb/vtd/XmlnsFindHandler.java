/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-10-5
 */
public class XmlnsFindHandler extends DefaultHandler {

	private List<String> lostNs = new ArrayList<String>();
	private Stack<String> ndStack = new Stack<>();

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		ndStack.push(qName);
		int p = qName.indexOf(':');
		String parentsName = "";
		if (p > 0) {
			ndStack.pop();
			ndStack.push(qName.substring(p + 1));
			parentsName = getParentsName();
			String prefix = qName.substring(0, p);
			if (!lostNs.contains(prefix + "_" + parentsName))
				lostNs.add(prefix + "_" + parentsName);
		}
		List<String> declaredNs = new ArrayList<>();
		for (int i=0; i<atts.getLength(); i++) {
			String attName = atts.getQName(i);
			if (!attName.startsWith("xmlns")) {
				p = attName.indexOf(':');
				if (p > 0) {
					String prefix = attName.substring(0, p);
					parentsName = getParentsName();
					if (!lostNs.contains(prefix + "_" + parentsName))
						lostNs.add(prefix + "_" + parentsName);
				}
			} else {
				p = attName.indexOf(':');
				if (p > 0) {
					String prefix = attName.substring(p + 1);
					parentsName = getParentsName();
					if (!declaredNs.contains(prefix + "_" + parentsName))
						declaredNs.add(prefix + "_" + parentsName);
				}
			}
		}
		for (String ns : declaredNs) {
			lostNs.remove(ns);
		}
	}

	private String getParentsName() {
		String parentsName = "";
		Stack<String> ndStackTemp = new Stack<>();
		if (!ndStack.isEmpty()) {
			String parent = ndStack.pop();
			parentsName = parent;
			ndStackTemp.push(parent);
			if (!ndStack.isEmpty()) {
				parent = ndStack.pop();
				parentsName = parent + "/" + parentsName;
				ndStackTemp.push(parent);
				if (!ndStack.isEmpty()) {
					parent = ndStack.pop();
					parentsName = parent + "/" + parentsName;
					ndStackTemp.push(parent);
					ndStack.push(ndStackTemp.pop());
				}
				ndStack.push(ndStackTemp.pop());
			}
			ndStack.push(ndStackTemp.pop());
		}
		return parentsName;
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		ndStack.pop();
	}

	public List<String> getLostNs() {
		return lostNs;
	}

}

