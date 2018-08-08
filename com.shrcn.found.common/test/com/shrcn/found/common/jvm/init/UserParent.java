/**
 * Copyright (c) 2007-2015 思源电气股份有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.jvm.init;

 /**
 * -XX:+ThraceClassLoading
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-10-13
 */
public class UserParent {
	public static void main(String[] args){

		System.out.println(Child.v);
		
		String a = "aa";
		String b = "bb";
		String c = "cc";
		
		switch(b) {
		case "aa":
			System.out.println(1);
			break;
		case "bb":
			System.out.println(2);
			break;
		case "cc":
			System.out.println(3);
			break;
		}

	}
}

