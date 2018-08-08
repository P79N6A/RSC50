/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import com.shrcn.found.common.util.ListUtil;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-19
 */
public class ListUtilTest {

	@Test
	public void testMoveTo() {
		fail("尚未实现");
	}

	@Test
	public void testSwap() {
		fail("尚未实现");
	}

	@Test
	public void testIsEmpty() {
		fail("尚未实现");
	}

	@Test
	public void testSortStr() {
		fail("尚未实现");
	}

	@Test
	public void testSearch() {
		List<String> list = new ArrayList<>();
		list.add("aaa");
		list.add("bbb");
		list.add("ccc");
		list.add("ddd");
		list.add("eee");
		assertEquals(4, ListUtil.search(list, "ee"));
		assertEquals(1, ListUtil.search(list, "bb"));
		assertEquals(3, ListUtil.search(list, "dd"));
	}

	@Test
	public void testSort() {
		int[] a1 = new int[] {72, 6, 57, 88, 60, 42, 83, 73, 48, 85};
		List<Integer> list1 = new ArrayList<>();
		List<Integer> list2 = new ArrayList<>();
		for (int a : a1) {
			list1.add(a);
			list2.add(a);
		}
		Comparator<Integer> c = new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}};
		java.util.Collections.sort(list1, c);
		ListUtil.sort(list2, c);
		java.util.Arrays.sort(a1);
		for (int i = 0; i<a1.length; i++) {
			assertEquals(a1[i], list2.get(i).intValue());
		}
	}
}

