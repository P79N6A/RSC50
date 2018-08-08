/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.jvm.test;

import static java.lang.invoke.MethodHandles.lookup;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-11-2
 */
public class MethodHandleTest1 {
	class GrandFather {
		void thinking() {
			System.out.println("i am grandfather");
		}
	}
	class Father extends GrandFather {
		void thinking() {
			System.out.println("i am father");
		}
	}
	class Son extends Father {
		void thinking() {
			try {
				MethodType mt = MethodType.methodType(void.class);
				MethodHandle mh = lookup().findSpecial(GrandFather.class, "thinking", mt, getClass());
				mh.invoke(this);
			} catch(Throwable e) {
				
			}
		}
	}
	public static void main(String[] args) {
		(new MethodHandleTest1().new Son()).thinking();
	}
}

