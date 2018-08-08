/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.xml.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.shrcn.found.file.xml.DOM4JNodeHelper;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-11-24
 */
public class DOM4JNodeHelperTest {

	@Test
	public void testCreateSCLNode() {
		fail("尚未实现");
	}

	@Test
	public void testCreateDocument() {
		fail("尚未实现");
	}

	@Test
	public void testGetAttributeElementString() {
		fail("尚未实现");
	}

	@Test
	public void testGetAttributeInt() {
		fail("尚未实现");
	}

	@Test
	public void testGetAttributeInt16() {
		fail("尚未实现");
	}

	@Test
	public void testGetAttributeFloat() {
		fail("尚未实现");
	}

	@Test
	public void testGetAttributeBoolean() {
		fail("尚未实现");
	}

	@Test
	public void testClearAttributes() {
		fail("尚未实现");
	}

	@Test
	public void testCopyAttributes() {
		fail("尚未实现");
	}

	@Test
	public void testGetAttributeValue() {
		fail("尚未实现");
	}

	@Test
	public void testGetAttributeValues() {
		fail("尚未实现");
	}

	@Test
	public void testGetAttributesElementString() {
		fail("尚未实现");
	}

	@Test
	public void testCreateRTUNode() {
		fail("尚未实现");
	}

	@Test
	public void testCreateSCLNamespaceNode() {
		fail("尚未实现");
	}

	@Test
	public void testParseText2Node() {
		fail("尚未实现");
	}

	@Test
	public void testNode2String() {
		fail("尚未实现");
	}

	@Test
	public void testNode2StringWithFormat() {
		fail("尚未实现");
	}

	@Test
	public void testGetAttributeByXPath() {
		fail("尚未实现");
	}

	@Test
	public void testGetNodeValueByXPath() {
		fail("尚未实现");
	}

	@Test
	public void testGetNodeValuesByXPath() {
		fail("尚未实现");
	}

	@Test
	public void testSelectSingleNode() {
		fail("尚未实现");
	}

	@Test
	public void testExistsNode() {
		fail("尚未实现");
	}

	@Test
	public void testSelectNodes() {
		fail("尚未实现");
	}

	@Test
	public void testAppendPrefix() {
		String xpath1 = "/scl:SCL/scl:IED[@name='net1']//scl:SubNetwork/scl:ConnectedAP";
		String xpath2 = "/scl:SCL/scl:Communication/scl:SubNetwork[count(./scl:Address)>0]/scl:ConnectedAP[count(./scl:Address)>0]/@iedName";
		
		assertEquals(xpath1, DOM4JNodeHelper.appendPrefix(DOM4JNodeHelper.clearPrefix(xpath1)));
		assertEquals(xpath2, DOM4JNodeHelper.appendPrefix(DOM4JNodeHelper.clearPrefix(xpath2)));
	}

	@Test
	public void testAsDOMDocument() {
		fail("尚未实现");
	}

	@Test
	public void testClearPrefix() {
		fail("尚未实现");
	}

	@Test
	public void testClearDefaultNS() {
		fail("尚未实现");
	}

	@Test
	public void testCheckStyle() {
		fail("尚未实现");
	}

	@Test
	public void testGetImportElement() {
		fail("尚未实现");
	}

	@Test
	public void testAddChildren() {
		fail("尚未实现");
	}

	@Test
	public void testAddText() {
		fail("尚未实现");
	}

	@Test
	public void testGetText() {
		fail("尚未实现");
	}

	@Test
	public void testAddAttributesElementObject() {
		fail("尚未实现");
	}

	@Test
	public void testAddAttributesElementObjectStringArray() {
		fail("尚未实现");
	}

	@Test
	public void testAssignAttValues() {
		fail("尚未实现");
	}

	@Test
	public void testAddAttribute() {
		fail("尚未实现");
	}

	@Test
	public void testGetRootElementString() {
		fail("尚未实现");
	}

	@Test
	public void testGetRootElementClassOfQString() {
		fail("尚未实现");
	}

	@Test
	public void testAsXML() {
		fail("尚未实现");
	}

	@Test
	public void testGetNodesContent() {
		fail("尚未实现");
	}

	@Test
	public void testMain() {
		fail("尚未实现");
	}

	@Test
	public void testInsertBefore() {
		fail("尚未实现");
	}

	@Test
	public void testInsertAfter() {
		fail("尚未实现");
	}

	@Test
	public void testInsertAsFirst() {
		fail("尚未实现");
	}

	@Test
	public void testInsertAsLast() {
		fail("尚未实现");
	}

	@Test
	public void testReplaceNode() {
		fail("尚未实现");
	}

	@Test
	public void testDeleteNodes() {
		fail("尚未实现");
	}

	@Test
	public void testSaveAttribute() {
		fail("尚未实现");
	}

	@Test
	public void testGetAttributeElementStringString() {
		fail("尚未实现");
	}

	@Test
	public void testGetAttributesElementStringString() {
		fail("尚未实现");
	}

	@Test
	public void testGetNodeValue() {
		fail("尚未实现");
	}

	@Test
	public void testUpdate() {
		fail("尚未实现");
	}

	@Test
	public void testSelectNodesOnlyAtts() {
		fail("尚未实现");
	}

	@Test
	public void testCountNodes() {
		fail("尚未实现");
	}

}

