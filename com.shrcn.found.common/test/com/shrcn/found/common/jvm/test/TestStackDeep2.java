/**
 * Copyright (c) 2007-2015 思源电气股份有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.jvm.test;

/**
 * 一个recursion函数含有3个参数和10个局部变量，因此，其局部变量表含有13个变量，
 * 而第二个recursion函数不再含有任何参数和局部变量，当这两个函数被嵌套调用时，
 * 第二个recursion函数可以拥有更深的调用层次。
 * -Xss128K
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-11-6
 */
public class TestStackDeep2 {
	private static int count = 0;

	public static void recursion(long a, long b, long c) {
		long e = 1, f = 2, g = 3, h = 4, i = 5, k = 6, q = 7, x = 8, y = 9, z = 10;
		count++;
		recursion(a, b, c);
	}

	public static void recursion() {
		count++;
		recursion();
	}

	public static void main(String[] args) {
		try {
//			recursion(0L, 0L, 0L);
			 recursion();
		} catch (Throwable e) {
			System.out.println("deep of calling = " + count);
			e.printStackTrace();
		}
	}
}
