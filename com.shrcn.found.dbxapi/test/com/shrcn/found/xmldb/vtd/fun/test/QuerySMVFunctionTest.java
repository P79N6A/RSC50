/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd.fun.test;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.junit.BeforeClass;
import org.junit.Test;

import com.shrcn.found.dbxapi.util.TestUtil;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.found.xmldb.vtd.fun.QuerySMVFunction;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-10-13
 */
public class QuerySMVFunctionTest {

	@BeforeClass
	public static void init() {
		String scdfile = "./test/SCD1-6.scd";
		TestUtil.init(scdfile, false);
	}
	
	@Test
	public void testExec() {
		Map<String, Object> params = new HashMap<String, Object>();
		List<Element> iedAps = (List<Element>) XMLDBHelper.callFunction(QuerySMVFunction.class, params);
		assertTrue(iedAps.size() > 1);
		System.out.println(iedAps.get(0).asXML());
	}

}

