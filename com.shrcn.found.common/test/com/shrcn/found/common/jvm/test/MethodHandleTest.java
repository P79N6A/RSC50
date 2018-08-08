/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.jvm.test;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-11-2
 */
public class MethodHandleTest {
	@Override
	public String toString() {

		return super.toString() + "==MethodHandle";
	}

	public static void main(String[] args) throws Throwable {
		MethodHandleTest handle = new MethodHandleTest();
		MethodType methodType = MethodType.methodType(String.class);

		Lookup lookup = MethodHandles.lookup();

		java.lang.invoke.MethodHandle methodHandle = lookup.findVirtual(MethodHandleTest.class, "toString", methodType);
		String toString = (String) methodHandle.invokeExact(handle);
		System.out.println(toString);// com.doctor.java7.MethodHandle@355da254==MethodHandle

		// or like this:
		java.lang.invoke.MethodHandle methodHandle2 = methodHandle.bindTo(handle);
		String toString2 = (String) methodHandle2.invokeWithArguments();
		System.out.println(toString2);// com.doctor.java7.MethodHandle@355da254==MethodHandle

		// 得到当前Class的不同表示方法，最后一个最好。一般我们在静态上下文用SLF4J得到logger用。
		System.out.println(MethodHandleTest.class);
		System.out.println(handle.getClass());
		System.out.println(MethodHandles.lookup().lookupClass()); // like getClass()
	}
}


