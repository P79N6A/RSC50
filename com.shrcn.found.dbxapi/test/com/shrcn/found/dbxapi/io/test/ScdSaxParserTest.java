/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.dbxapi.io.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.shrcn.found.common.util.TimeCounter;
import com.shrcn.found.dbxapi.io.ScdSaxParser;
import com.shrcn.found.dbxapi.io.ScdSection;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-7
 */
public class ScdSaxParserTest {

	@Test
	public void testParseScdFile() {
		TimeCounter.begin();
		System.gc();
		System.gc();
//		String scdfile = "E:/work/检测/六统一/测试数据/SCD-电科院0/scd(1-3)_模型实例与模板不匹配.scd";
		String scdfile = "E:/work/检测/六统一/测试数据/SCD-电科院0/测试用SCD-3/scd(1-1)_1000个ied.scd";
//		String scdfile = "C:/Documents and Settings/chenchun/桌面/ZJHHB201507221630.scd";
		ScdSaxParser parser = ScdSaxParser.getInstance();
		boolean success = parser.parseScdFile(scdfile);
		assertTrue(success);
		
//		Document doc = XMLFileManager.readXml(scdfile);
//		assertNotNull(doc);
		TimeCounter.end("SCD解析完毕");
		
//		System.out.println(
//			parser.getPart(ScdSection.Communication).asXML());
	}

}

