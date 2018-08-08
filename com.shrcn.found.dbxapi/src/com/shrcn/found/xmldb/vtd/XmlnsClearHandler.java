/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-10-5
 */
public class XmlnsClearHandler implements ContentHandler {

	private StringBuilder doc = new StringBuilder(1024);
	
	@Override
	public void setDocumentLocator(Locator locator) {
		
		
	}

	@Override
	public void startDocument() throws SAXException {
		
		
	}

	@Override
	public void endDocument() throws SAXException {
		
		
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
//		System.out.println(prefix + "--->" + uri);
		
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
//		System.out.println(prefix + "--->");
		
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		String ndName = qName;
		int p = ndName.indexOf(':');
		if (p > 0) {
			ndName = ndName.substring(p + 1);
		}
		doc.append("<" + ndName);
		for (int i=0; i<atts.getLength(); i++) {
			String attName = atts.getQName(i);
			if (!attName.startsWith("xmlns")) {
				p = attName.indexOf(':');
				if (p > 0) {
					attName = attName.substring(p + 1);
				}
				doc.append(" " + attName + "=\"" + atts.getValue(i) + "\"");
			}
		}
		doc.append(">");
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		String ndName = qName;
		int p = ndName.indexOf(':');
		if (p > 0) {
			ndName = ndName.substring(p + 1);
		}
		doc.append("</" + ndName + ">");
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		doc.append(new String(ch, start, length));
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		
		
	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		
		
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		
		
	}

	public String getClearXML() {
		return doc.toString();
	}
}

