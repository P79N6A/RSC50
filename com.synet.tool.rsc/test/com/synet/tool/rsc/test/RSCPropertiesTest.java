/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.synet.tool.rsc.RSCProperties;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-15
 */
public class RSCPropertiesTest {

	@Test
	public void testNextTbCode() {
		RSCProperties rscp = RSCProperties.getInstance();
		for (int i=0; i<25; i++) {
			String id = rscp.nextTbCode("IED");
			System.out.println(id);
		}
	}

}

