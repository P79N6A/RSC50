/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.shrcn.found.file.xml.XMLFileManager;
import com.shrcn.found.xmldb.vtd.XmlnsFindHandler;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-10-9
 */
public class XmlnsFindHandlerTest {

	@Test
	public void testGetLostNs() {
		String path = NameSpaceHandlerTest.class.getPackage().getName().replace(".", "/");
		XmlnsFindHandler handler = new XmlnsFindHandler();
		XMLFileManager.parseUTF8BySax(getClass(), path + "/oldpo.xml", handler);
		assertEquals(2, handler.getLostNs().size());
	}

}

