/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.dbxapi.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.file.xml.DOM4JNodeHelper;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-7
 */
public class ScdSaxParser {
	
	private OpenByStringHandler handler;
	private String msg;
	private static ScdSaxParser inst;
	
	private ScdSaxParser() {}
	
	public static ScdSaxParser getInstance() {
		if (inst == null) {
			inst = new ScdSaxParser();
		}
		return inst;
	}

	public boolean parseScdFile(String scdfile) {
		msg = null;
		FileInputStream fis = null;
		try {
			if (handler == null) {
				handler = new OpenByStringHandler();
			} else {
				handler.reset();
			}
			// 1、创建工厂  
	        SAXParserFactory factory = SAXParserFactory.newInstance();  
	        // 2、得到解析器  
	        SAXParser saxParser = factory.newSAXParser();  
	        // 3、得到读取器  
	        XMLReader reader = saxParser.getXMLReader();  
	        reader.setContentHandler(handler);
	        fis = new FileInputStream(new File(scdfile));
			InputSource is = new InputSource(fis);
	        is.setEncoding(Constants.CHARSET_UTF8);
	        reader.parse(is);
		} catch (SAXParseException exception) {
			msg = "行" + exception.getLineNumber() + "，列" + exception.getColumnNumber() + "："
					+ exception.getMessage();
			SCTLogger.error(msg, exception);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			msg = e.getMessage();
			SCTLogger.error(msg, e);
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				msg = e.getMessage();
				SCTLogger.error(msg, e);
			}
		}
		if (msg != null) {
			return false;
		}
		return true;
	}
	
	public String getErrMsg() {
		return this.msg;
	}
	
	public List<String[]> getIeds() {
		return handler.getIeds();
	}

	public List<String> getSubNets() {
		return handler.getSubNets();
	}
	
	public Element getPart(ScdSection sec) {
		String content = handler.getPart(sec.name());
		return content==null ? null : getContent(content);
	}

	public Element getIedPart(String iedName) {
		String content = handler.getPart(ScdSection.IED.name() + "_" + iedName);
		return content==null ? null : getContent(content);
	}

	private Element getContent(String content) {
		Element rootEl = (Element) DOM4JNodeHelper.parseText2Node(content);
		return (Element) rootEl.elements().get(0);
	}
	
	public void clearCache() {
		handler.reset();
	}
}

