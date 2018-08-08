/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.util.test;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;


 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-25
 */
public interface TestJNA extends StdCallLibrary {

	public abstract int add(int a, int b);
	
	TestJNA INSTANCE = (TestJNA) Native.loadLibrary("test", TestJNA.class);
}

