/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd.test;

import org.junit.Test;

import com.shrcn.found.file.util.FileManager;
import com.shrcn.found.file.xml.XMLFileManager;
import com.shrcn.found.xmldb.vtd.XmlnsClearHandler;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-10-5
 */
public class NameSpaceHandlerTest {

	@Test
	public void test() {
		String path = NameSpaceHandlerTest.class.getPackage().getName().replace(".", "/");
		XmlnsClearHandler handler = new XmlnsClearHandler();
		XMLFileManager.parseUTF8BySax(getClass(), path + "/oldpo.xml", handler);
		FileManager.saveTextFile("./clear.xml", handler.getClearXML(), "UTF-8");
	}

}

