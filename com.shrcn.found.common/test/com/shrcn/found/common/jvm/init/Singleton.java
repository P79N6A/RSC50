/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.jvm.init;

import java.util.HashMap;
import java.util.Map;

public class Singleton {
	public static Singleton singleton = new Singleton();
	public static Map m;
	static {
		m = new HashMap();
	}

	private Singleton() {
		initM();
	}

	public static void initM() {
		if (null == m) {
			System.out.println("m 为空");
			m = new HashMap();
		}
		m.put("1", "郑");
		m.put("2", "陈");
	}

	public static Singleton getInstance() {
		return singleton;
	}
	
	public static void main(String[] args) {
		Singleton singleton = Singleton.getInstance();
		System.out.print(Singleton.m.size());
	}
}
