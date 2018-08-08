/**
 * Copyright (c) 2007-2015 思源电气股份有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.jvm.test;

 /**
  * -Xss128K、-Xss256K
  * 函数嵌套调用的层次在很大程度上由栈的大小决定，栈越大，函数支持的嵌套调用次数就越多。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-11-6
 */
public class TestStackDeep {
	private static int count = 0;

	public static void recursion() {
		count++;
		recursion();
	}

	public static void main(String[] args) {
		try {
			recursion();
		} catch (Throwable e) {
			System.out.println("deep of calling =" + count);
			e.printStackTrace();
		}
	}
}

