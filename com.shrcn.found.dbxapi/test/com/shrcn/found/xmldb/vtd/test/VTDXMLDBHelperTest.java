/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.dom4j.Element;
import org.junit.BeforeClass;
import org.junit.Test;

import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.vtd.VTDXMLDBHelper;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-10-4
 */
public class VTDXMLDBHelperTest {
	
	private static String scdfile = "./test/SCD1-6.scd";
	private static String outPath = "E:\\SCD1-6.scd";
	private static VTDXMLDBHelper helper;
	
	@BeforeClass
	public static void init() {
		helper = new VTDXMLDBHelper();
		helper.loadDocument(null, scdfile);
		helper.setAutoCommit(true);
	}

	@Test
	public void testExportFormatedDocStringStringString() {
		helper.exportFormatedDoc(null, null, outPath);
		assertOutFile();
	}

	public void assertOutFile() {
		File file = new File(outPath);
		assertTrue(file.exists() && file.length()>10000);
	}

	@Test
	public void testExportFormatedDocString() {
		helper.setAutoCommit(false);
		String content = "<Header id=\"PRS-702A-DA-G-RPLDY\" nameStructure=\"IEDName\" revision=\"\" version=\"V2.00\"/>";
		Element node = DOM4JNodeHelper.parseText2Node(content);
		helper.replaceNode("/SCL/Header", node);
		helper.setAutoCommit(true);
		helper.exportFormatedDoc(outPath);
		assertOutFile();
	}

	@Test
	public void testSelectNodesString() {
		List<Element> ieds = helper.selectNodes("/SCL/IED[@name='PRS-702A-DA-G']");
		assertTrue(ieds.size()==1);
	}
	
	@Test
	public void testSelectNodesOnlyAtts() {
		List<Element> ieds = helper.selectNodesOnlyAtts("/SCL/IED", "IED");
		assertTrue(ieds.size()==9);
		for (Element ied : ieds) {
			assertTrue(ied.content().isEmpty());
		}
	}

	@Test
	public void testSelectNodesStringIntInt() {
		List<Element> ieds = helper.selectNodes("/SCL/IED", 2, 3);
		assertTrue(ieds.size()==3);
		assertEquals("PRS-7789-1", ieds.get(0).attributeValue("name"));
	}

	@Test
	public void testSelectSingleNode() {
		assertNotNull(helper.selectSingleNode("/SCL/IED[@name='PRS-7789-1']"));
	}

	@Test
	public void testExistsNode() {
		assertNotNull(helper.existsNode("/SCL/IED[@name='PRS-7789-1']"));
	}

	@Test
	public void testCountNodes() {
		assertTrue(helper.countNodes("/SCL/IED")==9);
	}
	
	@Test
	public void testInsertBeforeStringElement() {
	}

	@Test
	public void testInsertBeforeStringString() {
		String crc = "<Private type=\"Substation virtual terminal conection CRC\">537c4eb7</Private>";
		helper.insertBefore("/SCL/Header", crc);
		assertTrue(helper.countNodes("/SCL/Private") > 1);
	}

	@Test
	public void testInsertAfterStringElement() {
	}

	@Test
	public void testInsertAfterStringString() {
		String content = "<PhysConn type=\"RedConn\">\n" +
							"<P type=\"Port\">10-B</P>\n" +
							"<P type=\"Type\">FOC</P>\n" +
						"</PhysConn>";
		String select = "/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']" +
				"/ConnectedAP[@iedName='PRS-702A-DA-G'][@apName='S1']/PhysConn[@type='Connection']";
		helper.insertAfter(select , content);
		select = "/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']" +
				"/ConnectedAP[@iedName='PRS-702A-DA-G'][@apName='S1']/PhysConn[@type='RedConn']";
		assertNotNull(helper.selectSingleNode(select));
	}

	@Test
	public void testInsertAfterType() {
		String content = "<PhysConn type=\"RedConn\">\n" +
							"<P type=\"Port\">10-B</P>\n" +
							"<P type=\"Type\">FOC</P>\n" +
						"</PhysConn>";
		String parentXPath = "/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']" +
				"/ConnectedAP[@iedName='PRS-711-D'][@apName='S1']";
		String[] types = new String[] {"Address", "PhysConn"};
		Element node = DOM4JNodeHelper.parseText2Node(content);
		helper.insertAfterType(parentXPath, types, node);
		String select = "/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']" +
				"/ConnectedAP[@iedName='PRS-711-D'][@apName='S1']/PhysConn[@type='RedConn']";
		assertNotNull(helper.selectSingleNode(select));
		
		select = "/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']" +
				"/ConnectedAP[@iedName='PRS-711-D'][@apName='S1']/PhysConn";
		List<Element> nds = helper.selectNodes(select);
		Element lastNode = nds.get(nds.size()-1);
		assertEquals("RedConn", lastNode.attributeValue("type"));
	}

	@Test
	public void testInsertAsFirstStringElement() {
	}

	@Test
	public void testInsertAsFirstStringString() {
		String content = "<ConnectedAP apName=\"S1\" iedName=\"PRS-702A-DA-G1\">\n" +
					"<Address>\n" +
						"<P type=\"IP\">222.111.112.205</P>\n" +
						"<P type=\"IP-SUBNET\">255.255.255.0</P>\n" +
					"</Address>\n" +
				"</ConnectedAP>";
		helper.insertAsFirst("/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']", content);
		String select = "/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']" +
				"/ConnectedAP[@iedName='PRS-702A-DA-G1'][@apName='S1']";
		assertNotNull(helper.selectSingleNode(select));
		
		select = "/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']" +
				"/ConnectedAP";
		List<Element> nds = helper.selectNodes(select);
		Element firstNode = nds.get(0);
		assertEquals("PRS-702A-DA-G1", firstNode.attributeValue("iedName"));
	}

	@Test
	public void testInsertAsLastStringElement() {
	}

	@Test
	public void testInsertAsLastStringString() {
		String content = "<ConnectedAP apName=\"S1\" iedName=\"PRS-702A-DA-G2\">\n" +
					"<Address>\n" +
						"<P type=\"IP\">222.111.112.205</P>\n" +
						"<P type=\"IP-SUBNET\">255.255.255.0</P>\n" +
					"</Address>\n" +
				"</ConnectedAP>";
		helper.insertAsLast("/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']", content);
		String select = "/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']" +
				"/ConnectedAP[@iedName='PRS-702A-DA-G1'][@apName='S1']";
		assertNotNull(helper.selectSingleNode(select));
		
		select = "/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']" +
				"/ConnectedAP";
		List<Element> nds = helper.selectNodes(select);
		Element firstNode = nds.get(nds.size()-1);
		assertEquals("PRS-702A-DA-G2", firstNode.attributeValue("iedName"));
	}

	@Test
	public void testMoveDown() {
		String select = "/SCL/IED";
		helper.moveDown(select, 1);
		List<Element> nds = helper.selectNodes(select);
		Element firstNode = nds.get(0);
		assertEquals("PRS-7393-1", firstNode.attributeValue("name"));
	}

	@Test
	public void testMoveUp() {
		String select = "/SCL/DataTypeTemplates/LNodeType";
		helper.moveUp(select, 2);
		List<Element> nds = helper.selectNodes(select);
		Element firstNode = nds.get(0);
		assertEquals("SZNR_V1.00_LLN0_PROT", firstNode.attributeValue("id"));
	}

	@Test
	public void testMoveTo() {
		String select = "/SCL/DataTypeTemplates/LNodeType";
		helper.moveTo(select, 5, 1);
		List<Element> nds = helper.selectNodes(select);
		Element firstNode = nds.get(0);
		assertEquals("SZNR_V1.00_PTRC", firstNode.attributeValue("id"));
	}

	@Test
	public void testInsertStringElement() {
	}

	@Test
	public void testInsertStringString() {
		String content = "<SubNetwork name=\"net2\"/>";
		helper.insert("/SCL/Communication", content);
		assertNotNull(helper.selectSingleNode("/SCL/Communication/SubNetwork[@name='net2']"));
	}

	@Test
	public void testReplaceNode() {
		String content = "<Header id=\"PRS-702A-DA-G-RPLDY\" nameStructure=\"IEDName\" revision=\"\" version=\"V2.00\"/>";
		Element node = DOM4JNodeHelper.parseText2Node(content);
		helper.replaceNode("/SCL/Header", node);
		assertNotNull(helper.selectSingleNode("/SCL/Header[@version='V2.00']"));
	}

	@Test
	public void testDeleteNodes() {
		String select = "/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']" +
				"/ConnectedAP[@iedName='PRS-711-D-M'][@apName='S1']/PhysConn";
		helper.deleteNodes(select);
		assertNull(helper.selectSingleNode(select));
	}

	@Test
	public void testSaveAttribute() {
		String ndXpath = "/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']" +
				"/ConnectedAP[@iedName='PRS-711-D-M'][@apName='S1']";
		String attName = "apName";
		String value = "S2";
		helper.saveAttribute(ndXpath, attName, value);
		assertNull(helper.selectSingleNode(ndXpath));
	}

	@Test
	public void testGetAttributeValue() {
		String select = "/SCL/Communication/SubNetwork[@name='SMV']/@type";
		String type = helper.getAttributeValue(select);
		assertEquals(type, "8-MMS");
	}

	@Test
	public void testGetAttributeValues() {
		String select = "/SCL/Communication/SubNetwork/@name";
		List<String> types = helper.getAttributeValues(select);
		assertTrue(types.size() > 3);
		
		List<String> ieds = helper.getAttributeValues("/SCL/IED[@name!='PRS-711-D-M']/@name");
		assertTrue(ieds.indexOf("PRS-711-D-M")<0);
		
		ieds = helper.getAttributeValues("/SCL/IED/@name");
		assertTrue(ieds.indexOf("PRS-711-D-M")>-1);
	}

	@Test
	public void testGetNodeValue() {
		String select = "/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']" +
				"/ConnectedAP[@iedName='PRS-711-D-M']/Address/P[@type='IP-SUBNET']";
		assertEquals("255.255.255.0", helper.getNodeValue(select));
	}

	@Test
	public void testAppendAttribute() {
		String ndXpath = "/SCL/Communication/SubNetwork[@name='net1']";
		helper.setAutoCommit(false);
		helper.appendAttribute(ndXpath, "type", "GOOSE");
		helper.appendAttribute(ndXpath, "type1", "GOOSE1");
		assertEquals("GOOSE", helper.getAttributeValue("/SCL/Communication/SubNetwork[@name='net1']/@type"));
		helper.forceCommit();
		assertEquals("GOOSE1", helper.getAttributeValue("/SCL/Communication/SubNetwork[@name='net1']/@type1"));
		helper.setAutoCommit(true);
	}

	@Test
	public void testUpdate() {
		String select = "/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']" +
				"/ConnectedAP[@iedName='PRS-711-D-M']/Address/P[@type='IP']";
		helper.update(select, "192.168.0.1");
		assertEquals("192.168.0.1", helper.getNodeValue(select));
	}

	@Test
	public void testInsertNodeByDoc() {
		fail("尚未实现");
	}

	@Test
	public void testReplaceNodeByDoc() {
		fail("尚未实现");
	}

	@Test
	public void testGetAttributeStrings() {
		fail("尚未实现");
	}

	@Test
	public void testQueryNodes() {
		fail("尚未实现");
	}

	@Test
	public void testQueryAttributes() {
		fail("尚未实现");
	}

	@Test
	public void testQueryAttribute() {
		fail("尚未实现");
	}

}

