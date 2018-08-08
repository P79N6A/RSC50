/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.util.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.shrcn.found.common.util.ArrayUtil;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-10-9
 */
public class ArrayUtilTest {

	@Test
	public void testQuickSort() {
		int[] a1 = new int[] {72, 6, 57, 88, 60, 42, 83, 73, 48, 85};
		int[] a2 = new int[] {72, 6, 57, 88, 60, 42, 83, 73, 48, 85};
		java.util.Arrays.sort(a1);
		ArrayUtil.sort(a2);
		for (int i = 0; i<a1.length; i++) {
			assertEquals(a1[i], a2[i]);
		}
	}

}

