/**
 * Copyright (c) 2007-2015 思源电气股份有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.jvm.init;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-10-13
 */
public class Alibaba {
	public static int k = 0;
	public static Alibaba t1 = new Alibaba("t1");
	public static Alibaba t2 = new Alibaba("t2");
	public static int i = print("i");
	public static int n = 99;
	
	private int a = 0;
	public int j = print("j");
	
	{
		print("构造块");
	}
	
	static {
		print("静态块");
	}

	public Alibaba(String str) {
		System.out.println((++k) + ":" + str + "   i=" + i + "    n=" + n);
		++i;
		++n;
	}

	public static int print(String str) {
		System.out.println((++k) + ":" + str + "   i=" + i + "    n=" + n);
		++n;
		return ++i;
	}

	public static void main(String args[]) {
		Alibaba t = new Alibaba("init");
	}

}
