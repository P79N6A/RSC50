/**
 * Copyright (c) 2007-2015 思源电气股份有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.instru.test;

 /**
 * -javaagent:hello.jar
 * Premain-Class: com.shrcn.found.instru.test.HelloWorld
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-10-25
 */
public class Example {
	public static void main(String[] args){
		System.out.println("main class of proxy!");
		System.out.println("o size=" + HelloWorld.size(new Object()));
		System.out.println("o size=" + HelloWorld.size(new HelloWorld()));
		System.out.println("o size=" + HelloWorld.size(new A()));
	}
}

