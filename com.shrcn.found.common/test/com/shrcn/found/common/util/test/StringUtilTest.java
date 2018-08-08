/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.util.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.shrcn.found.common.util.StringUtil;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-24
 */
public class StringUtilTest {

	@Test
	public void testGetFCFromOutSignal() {
		fail("尚未实现");
	}

	@Test
	public void testNullToEmpty() {
		fail("尚未实现");
	}

	@Test
	public void testEmptyToNull() {
		fail("尚未实现");
	}

	@Test
	public void testNullToInt() {
		fail("尚未实现");
	}

	@Test
	public void testIsEmpty() {
		fail("尚未实现");
	}

	@Test
	public void testIsNull() {
		fail("尚未实现");
	}

	@Test
	public void testVerifyNum() {
		fail("尚未实现");
	}

	@Test
	public void testTrimPrefix() {
		fail("尚未实现");
	}

	@Test
	public void testTrimZero() {
		fail("尚未实现");
	}

	@Test
	public void testToXMLChars() {
		fail("尚未实现");
	}

	@Test
	public void testClearGrammarChars() {
		fail("尚未实现");
	}

	@Test
	public void testClearMinus() {
		fail("尚未实现");
	}

	@Test
	public void testString2Boolean() {
		fail("尚未实现");
	}

	@Test
	public void testString2BigInteger() {
		fail("尚未实现");
	}

	@Test
	public void testString2Integer() {
		fail("尚未实现");
	}

	@Test
	public void testStr2int() {
		fail("尚未实现");
	}

	@Test
	public void testGetFormatStr() {
		fail("尚未实现");
	}

	@Test
	public void testGetBooleanStr() {
		fail("尚未实现");
	}

	@Test
	public void testGetBoolean() {
		fail("尚未实现");
	}

	@Test
	public void testGetLabel() {
		fail("尚未实现");
	}

	@Test
	public void testGetName() {
		fail("尚未实现");
	}

	@Test
	public void testHasChinese() {
		fail("尚未实现");
	}

	@Test
	public void testIsValidDesc() {
		fail("尚未实现");
	}

	@Test
	public void testIsValidName() {
		fail("尚未实现");
	}

	@Test
	public void testGetLabelWithDefault() {
		fail("尚未实现");
	}

	@Test
	public void testGetCurrentTime() {
//		String pattern = "yyyy-MM-dd HH:mm:ss";
		String pattern = "yyyy-MM-dd HH:mm:ss";
		String datetime1 = "2017-04-12 16:30:23:111:123";
		String datetime2 = "2017-04-12 16:30:24:112:123";
		long time1 = StringUtil.getDate(datetime1.substring(0, pattern.length()), pattern).getTime();
		long time2 = StringUtil.getDate(datetime2.substring(0, pattern.length()), pattern).getTime();
		int offset = pattern.length();
		long interval = (time2-time1)*1000 + (getTimeMillis(datetime2, offset) - getTimeMillis(datetime1, offset));
		assertEquals(1001000, interval);
//		System.currentTimeMillis();
//		Date date = StringUtil.getDate(datetime, pattern);
//		assertEquals(datetime, StringUtil.getDateStr(date, pattern));
	}
	
	@Test
	public void testGetDateStr() {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		
		String t = StringUtil.getDateStr(new Date(1530374400), pattern);
		System.out.println(t);
	}
	
	private int getTimeMillis(String datetime, int offset) {
		datetime = datetime.substring(offset + 1);
		String[] times = datetime.split(":");
		return Integer.parseInt(times[0]) * 1000 + Integer.parseInt(times[1]);
	}

	@Test
	public void testGetNamePoint() {
		fail("尚未实现");
	}

	@Test
	public void testGetLastName() {
		fail("尚未实现");
	}

	@Test
	public void testGetFirstName() {
		fail("尚未实现");
	}

	@Test
	public void testCompare() {
		fail("尚未实现");
	}

	@Test
	public void testGetDoXpath() {
		fail("尚未实现");
	}

	@Test
	public void testGetFixMAC() {
		fail("尚未实现");
	}

	@Test
	public void testGetCleanDesc() {
		fail("尚未实现");
	}

	@Test
	public void testNativeToAscii() {
		fail("尚未实现");
	}

	@Test
	public void testAsciiToNative() {
		fail("尚未实现");
	}

	@Test
	public void testReplaceTabWithBlank() {
		fail("尚未实现");
	}

	@Test
	public void testGetChineseTime() {
		fail("尚未实现");
	}

	@Test
	public void testReplaceBlank() {
		fail("尚未实现");
	}

	@Test
	public void testFitLength() {
		fail("尚未实现");
	}

	@Test
	public void testGetMD5CodeForStr() {
		fail("尚未实现");
	}

	@Test
	public void testGetLCS() {
		fail("尚未实现");
	}

	@Test
	public void testIsNumeric() {
		fail("尚未实现");
	}

	@Test
	public void testGetNumeric() {
		fail("尚未实现");
	}

	@Test
	public void testGetIntegerValue() {
		fail("尚未实现");
	}

	@Test
	public void testCheckBooleanValid() {
		fail("尚未实现");
	}

	@Test
	public void testCheckIntegerValid() {
		fail("尚未实现");
	}

	@Test
	public void testCheckFloatValid() {
		fail("尚未实现");
	}

	@Test
	public void testString2Unicode() {
		String s = "我们是五月的花朵aaa111";
        
		String us = StringUtil.string2Unicode(s);
		System.out.println(us);
		System.out.println(StringUtil.unicode2String(us));
	}

	@Test
	public void testUnicode2String() {
		fail("尚未实现");
	}

	@Test
	public void testGetSimilar() {
		String x = "今天星期四";
		String y = "今天是星期五";
		double p1 = StringUtil.getSimilarByLCS(x, y);
		double p2 = StringUtil.getSimilarByLeven(x, y);
		double p3 = StringUtil.getSimilarity(x, y);
		System.out.println("LCS: " + p1);
		System.out.println("Leven: " + p2);
		System.out.println("Degree: " + p3);
		assertTrue(p1 > 0.3);
		assertTrue(p2 > 0.3);
		assertTrue(Math.abs(p1 - p2) < 0.01);
		assertTrue(Math.abs(p1 - p3) < 0.01);
	}
	
	@Test
	public void testIsValidXml() {
		String[] ss = new String[] {"asfsd<",
				"asfsd<>&",
				"asfsd\\",
				"asfsd\"",
				"asfsd/",
		};
		for (String s : ss) {
			assertFalse(StringUtil.isValidXml(s));
		}
	}
	
	@Test
	public void testInteger2Hex() {
		assertEquals("6201", StringUtil.integer2Hex(25089, ""));
		assertEquals("6201H", StringUtil.integer2Hex(25089, "H"));
	}
}

