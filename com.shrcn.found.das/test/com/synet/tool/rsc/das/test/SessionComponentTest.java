package com.synet.tool.rsc.das.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.synet.tool.rsc.das.RscDbManagerImpl;
import com.synet.tool.rsc.das.SessionComponent;

public class SessionComponentTest {

	private RscDbManagerImpl dbmgr = RscDbManagerImpl.getInstance();
	
	@Before
	public void init() {
//		dbmgr.
	}
	
	@Test
	public void testInitString() {
		String configURL = "com/synet/tool/rsc/das/hibernate.rsccfg.xml";
		
		SessionComponent sc = new SessionComponent(dbmgr);
		sc.init(configURL);
		assertNotNull(sc.getSessionFactory());
	}

}
