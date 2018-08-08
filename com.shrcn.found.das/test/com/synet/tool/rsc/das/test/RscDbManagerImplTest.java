package com.synet.tool.rsc.das.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.synet.tool.rsc.das.RscDbManagerImpl;

public class RscDbManagerImplTest {

	@Test
	public void testGetInstance() {
		RscDbManagerImpl.getInstance();
	}

}
