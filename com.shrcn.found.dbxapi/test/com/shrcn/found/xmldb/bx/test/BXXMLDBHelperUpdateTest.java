/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.bx.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.dom4j.Element;
import org.junit.BeforeClass;
import org.junit.Test;

import com.shrcn.found.common.Constants;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.DBBroker;
import com.shrcn.found.xmldb.ProjectManager;
import com.shrcn.found.xmldb.bx.XMLDBHelperImpl;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-10-4
 */
public class BXXMLDBHelperUpdateTest {
	
	private static String prjName = "SCD1-6-Fix";
	private static String scdfile = "./test/" + prjName + ".scd";
	private static String outPath = "E:\\" + prjName + ".scd";
	private static DBBroker helper;
	
	@BeforeClass
	public static void init() {
		helper = new XMLDBHelperImpl();
		Constants.DEFAULT_PRJECT_NAME = prjName;
		if (Constants.XQUERY) {
			if (ProjectManager.existProject(prjName)) {
				ProjectManager.closePrjectDB(prjName);
				ProjectManager.clearPrjectDB(prjName);
			}
			ProjectManager.initPrjectDB(prjName);
		}
		helper.loadDocument(Constants.DEFAULT_SCD_DOC_NAME, scdfile);
		FileManipulate.deleteFile(outPath);
	}
	
	///////////////////////////////////////////
	@Test
	public void testCountNodes() {
		assertTrue(helper.countNodes("/SCL/IED")==9);
//	}
//	
//	@Test
//	public void testInsertAfterStringString() {
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
//	}
//
//	@Test
//	public void testInsertAfterType() {
		content = "<PhysConn type=\"RedConn\">\n" +
							"<P type=\"Port\">10-B</P>\n" +
							"<P type=\"Type\">FOC</P>\n" +
						"</PhysConn>";
		String parentXPath = "/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']" +
				"/ConnectedAP[@iedName='PRS-711-D'][@apName='S1']";
		String[] types = new String[] {"Address", "PhysConn"};
		Element node = DOM4JNodeHelper.parseText2Node(content);
		helper.insertAfterType(parentXPath, types, node);
		select = "/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']" +
				"/ConnectedAP[@iedName='PRS-711-D'][@apName='S1']/PhysConn[@type='RedConn']";
		assertNotNull(helper.selectSingleNode(select));
		
		select = "/SCL/Communication/SubNetwork[@name='SubNetwork_Stationbus']" +
				"/ConnectedAP[@iedName='PRS-711-D'][@apName='S1']/PhysConn";
		List<Element> nds = helper.selectNodes(select);
		Element lastNode = nds.get(nds.size()-1);
		assertEquals("RedConn", lastNode.attributeValue("type"));
	}

}

