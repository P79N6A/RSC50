/**
 * Copyright (c) 2007-2015 思源电气股份有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.instru.test;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-10-25
 */
public class HelloWorld implements ClassFileTransformer {
	 
	private static Instrumentation inst;
	
	public static void premain(String args, Instrumentation inst){
		HelloWorld.inst = inst;
		inst.addTransformer(new HelloWorld());
	}

	@Override
	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer)
			throws IllegalClassFormatException {
		System.out.println("java.lang.instrument, hello world!");
		return null;
	}
	
	public static long size(Object o) {
		return inst.getObjectSize(o);
	}
	 
}
