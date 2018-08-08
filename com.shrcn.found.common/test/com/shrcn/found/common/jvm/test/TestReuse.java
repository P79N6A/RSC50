/**
 * Copyright (c) 2007-2015 思源电气股份有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.jvm.test;

/**
 * 显示局部变量表的复用。
 * 在localvar1函数中，局部变量a和b都作用到了函数的末尾，故b无法复用a所在的位置。
 * 而在localvar2()函数中，局部变量a在第？行不再有效，故局部变量b可以复用a的槽位（1个字）
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-11-6
 */
public class TestReuse {
	public static void localvar1() {
		int a = 0;
		System.out.println(a);
		int b = 0;
	}

	public static void localvar2() {
		{
			int a = 0;
			System.out.println(a);
		}
		int b = 0;
	}
}
