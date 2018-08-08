/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd.fun.test;

//import static org.junit.Assert.assertEquals;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.shrcn.found.common.Constants;
import com.shrcn.found.dbxapi.util.TestUtil;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.found.xmldb.vtd.fun.UpdateCRCFunction;

 /**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2015-11-4
 */
public class UpdateCRCFunctionTest {

	@BeforeClass
	public static void init() {
		String scdfile = "./test/SCD1-6.scd";
		TestUtil.init(scdfile, false);
	}
	
	@Test
	public void testExec() {
		String desc = Constants.IED_CRC_DESC;
		String xpath = "/SCL/IED[@name='PRS-702A-DA-G']";
		String value = "testCRC";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("xpath", xpath);
		params.put("desc", desc);
		params.put("value", value);
		XMLDBHelper.callUpdateFunction(UpdateCRCFunction.class, params);
		
		XMLDBHelper.exportFormatedDoc("D:\\1-1.scd");
		
		String nodeValue = XMLDBHelper.getNodeValue(xpath + "/Private[@type='" + desc + "']");
		assertEquals(nodeValue, value);
	}
}

