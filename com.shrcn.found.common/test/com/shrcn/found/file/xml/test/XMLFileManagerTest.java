/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.file.xml.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.dom4j.Document;
import org.junit.Test;

import com.shrcn.found.file.xml.XMLFileManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-3-12
 */
/**
 * $Log$
 */
public class XMLFileManagerTest {

	@Test
	public void testReadXml() {
		fail("尚未实现");
	}

	@Test
	public void testWriteDoc() {
		fail("尚未实现");
	}

	@Test
	public void testFormatDocument() {
		fail("尚未实现");
	}

	@Test
	public void testCreateDocumentFile() {
		fail("尚未实现");
	}

	@Test
	public void testCreateW3CDocument() {
		fail("尚未实现");
	}

	@Test
	public void testCreateDocumentString() {
		fail("尚未实现");
	}

	@Test
	public void testLoadXMLFileClassOfQString() {
		fail("尚未实现");
	}

	@Test
	public void testLoadXMLFileString() {
		Document doc = XMLFileManager.loadXMLFile("./test/device.cid");
		assertNotNull(doc);
		System.out.println(doc.asXML());
	}

	@Test
	public void testLoadXMLFileFile() {
		fail("尚未实现");
	}

	@Test
	public void testSaveDocument() {
		fail("尚未实现");
	}

	@Test
	public void testSaveUTF8Document() {
		fail("尚未实现");
	}

	@Test
	public void testSaveGB2312Document() {
		fail("尚未实现");
	}

	@Test
	public void testSaveUTF8FileStringFile() {
		fail("尚未实现");
	}

	@Test
	public void testSaveUTF8FileStringString() {
		fail("尚未实现");
	}

}
