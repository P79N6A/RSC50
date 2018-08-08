/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.dom4j.Element;
import org.junit.Test;

import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.vtd.VTDXPathUtil;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-11-23
 */
public class VTDXPathUtilTest {

//	@Test
//	public void testGetAttrMap() {
//		Map<String, String> attrMap = VTDXPathUtil.getAttrMap("/SCL/IED[@name='aaa'][@type='111']");
//		assertEquals(attrMap.size(), 2);
//		assertEquals(attrMap.get("name"), "aaa");
//		assertEquals(attrMap.get("type"), "111");
//	}
//
//	@Test
//	public void testMatchNode() {
//		Map<String, String> attrMap = VTDXPathUtil.getAttrMap("/SCL/IED[@name='aaa'][@type='111']");
////		for (Entry<String, String> att : attrMap.entrySet()) {
////			System.out.println(att.getKey() + ":" + att.getValue());
////		}
//		Element node = DOM4JNodeHelper.createRTUNode("SCL");
//		Element nodeIed = node.addElement("IED");
//		nodeIed.addAttribute("name", "aaa");
//		nodeIed.addAttribute("type", "111");
////		System.out.println(VTDXPathUtil.matchNode(nodeIed, attrMap));
//		assertTrue(VTDXPathUtil.matchNode(nodeIed, attrMap));
//	}
	
	@Test
	public void testClearFun() {
		String select = "/scl:SCL/scl:Communication/scl:SubNetwork[count(./scl:Address)>0]/scl:ConnectedAP[count(./scl:Address)>0]/@iedName";
		assertEquals(VTDXPathUtil.clearFun(select), "/scl:SCL/scl:Communication/scl:SubNetwork/scl:ConnectedAP/@iedName");
		
		String xPath = "/SCL/IED[@name='test']/AccessPoint/Server/LDevice[count(./*/GSEControl)>0 or count(./*/SampledValueControl)>0 or count(./*/ReportControl)>0]";
		assertEquals(VTDXPathUtil.clearFun(xPath), "/SCL/IED[@name='test']/AccessPoint/Server/LDevice");
		
		String xPath1 = "/scl:SCL/scl:IED[count(./scl:AccessPoint/scl:Server/scl:LDevice//scl:Inputs/scl:ExtRef[@iedName='CM103'])>0]";
		assertEquals(VTDXPathUtil.clearFun(xPath1), "/scl:SCL/scl:IED");
		
		String xPath2 = "/scl:SCL/scl:IED[count(./scl:AccessPoint/scl:Server/scl:LDevice)>0]";
		assertEquals(VTDXPathUtil.clearFun(xPath2), "/scl:SCL/scl:IED");
	}
	
	@Test
	public void testGetSubXPath() {
		String xpath0 = "/SCL/IED";
		String xpath1 = "/SCL/IED[@name='aaa'][@type='111']";
		String xpath2 = "/scl:SCL/scl:Communication/scl:SubNetwork[count(./scl:Address)>0]/scl:ConnectedAP[count(./scl:Address)>0]/@iedName";
		String xpath3 = "/SCL/IED[@name='test']";
		
		assertEquals(VTDXPathUtil.getSubXPath(xpath0), ".");
		assertEquals(VTDXPathUtil.getSubXPath(xpath1), ".[@name='aaa'][@type='111']");
		assertEquals(VTDXPathUtil.getSubXPath(xpath2), "./SubNetwork[count(./Address)>0]/ConnectedAP[count(./Address)>0]/@iedName");
		
		Element root = DOM4JNodeHelper.createSCLNode("SCL");
		Element iedNd = root.addElement("IED");
		iedNd.addAttribute("name", "test");
		iedNd.addAttribute("type", "111");
		assertNotNull(DOM4JNodeHelper.selectSingleNode(iedNd, VTDXPathUtil.getSubXPath(xpath3)));
		assertNotNull(DOM4JNodeHelper.selectSingleNode(iedNd, "/SCL/./IED[@name='test']"));
	}
	
	@Test
	public void testGetCacheXPath() {
		String xpath1 = "/scl:SCL/scl:IED[@name='net1']/scl:SubNetwork/scl:ConnectedAP";
		String xpath2 = "/scl:SCL/scl:IED[@name='net1'][count(./scl:Address)>0]/scl:ConnectedAP[count(./scl:Address)>0]/@iedName";
		assertEquals(VTDXPathUtil.getCacheXPath(xpath1), "/SCL/IED[@name='net1']");
		assertEquals(VTDXPathUtil.getCacheXPath(xpath2), "/SCL/IED[@name='net1']");
	}
	
	@Test
	public void testGetPartRootXPath() {
		String xpath1 = "/scl:SCL/scl:IED[@name='net1']/scl:SubNetwork/scl:ConnectedAP";
		String xpath2 = "/scl:SCL/scl:Communication/scl:SubNetwork[count(./scl:Address)>0]/scl:ConnectedAP[count(./scl:Address)>0]/@iedName";
		assertEquals(VTDXPathUtil.getPartRootXPath(xpath1), "/SCL/IED");
		assertEquals(VTDXPathUtil.getPartRootXPath(xpath2), "/SCL/Communication");
	}

	@Test
	public void testIsPartRoot() {
		String xpath1 = "/scl:SCL/scl:IED/scl:SubNetwork/scl:ConnectedAP";
		assertTrue(VTDXPathUtil.isPartRoot(xpath1));
		String xpath2 = "/scl:SCL/scl:IED[@name='net1']/scl:SubNetwork/scl:ConnectedAP";
		assertFalse(VTDXPathUtil.isPartRoot(xpath2));
	}
}

