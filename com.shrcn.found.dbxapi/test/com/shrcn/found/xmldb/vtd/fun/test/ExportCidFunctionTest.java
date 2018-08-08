/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd.fun.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.shrcn.found.dbxapi.util.TestUtil;
import com.shrcn.found.file.util.FileManager;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.found.xmldb.vtd.fun.ExportCidFunction;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-10-8
 */
public class ExportCidFunctionTest {
	
	@BeforeClass
	public static void init() {
		String scdfile = "./test/SCD1-6.scd";
		TestUtil.init(scdfile, false);
	}
	
	@Test
	public void testExec() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iedName", "PRS-702A-DA-G");
		String cid = (String) XMLDBHelper.callFunction(ExportCidFunction.class, params);
		assertTrue(cid.length() > 1000);
		String outpath = "./test.cid";
		FileManager.saveTextFile(outpath, cid, "UTF-8");
		assertTrue(new File(outpath).exists());
	}

}

