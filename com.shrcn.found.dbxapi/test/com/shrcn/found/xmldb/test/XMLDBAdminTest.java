/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.xmldb.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.shrcn.found.common.Constants;
import com.shrcn.found.xmldb.DBAdminBroker;
import com.shrcn.found.xmldb.DBBroker;
import com.shrcn.found.xmldb.bx.XMLDBAdminImpl;
import com.shrcn.found.xmldb.bx.XMLDBHelperImpl;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-5-1
 */
/**
 * $Log$
 */
public class XMLDBAdminTest {

	@Test
	public void testGetConnection() {
//		Constants.DEFAULT_PRJECT_NAME = "hecunbian_0428";
		Constants.DEFAULT_PRJECT_NAME = "Sub_JinNan_New";
		DBAdminBroker admin = new XMLDBAdminImpl();
		admin.getCollection();
		DBBroker dbHelper = new XMLDBHelperImpl();
		assertNotNull(dbHelper.selectSingleNode("/SCL/IED"));
	}

}
