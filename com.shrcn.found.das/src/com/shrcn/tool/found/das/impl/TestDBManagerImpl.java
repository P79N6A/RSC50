/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.found.das.impl;


 /**
 * 
 * @author 李蒙龙(mailto:202137/siyuan.com)
 * @version 1.0, 2017-3-23
 */
public class TestDBManagerImpl extends ToolDBManagerImpl {

	private static TestDBManagerImpl inst;

	public TestDBManagerImpl() {
		super();
	}
	
	public static TestDBManagerImpl getInstance() {
	if (inst == null)
		inst = new TestDBManagerImpl();
	return inst;
	}
	
	@Override
	protected void setProDbName() {
		prjDbName = "testdata";
		user = "testapp";
	}
	
}

