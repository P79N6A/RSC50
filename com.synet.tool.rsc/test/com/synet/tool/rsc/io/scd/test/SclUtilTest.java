package com.synet.tool.rsc.io.scd.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.synet.tool.rsc.io.scd.SclUtil;

public class SclUtilTest {
	String cbDataRef = "LD0/ALMGGIO1$ST$Alm6$stVal";
	String pinDataRef = "PI/NIGOINGGIO1.IntIn3.stVal";

	@Test
	public void testGetv() {
		String v = SclUtil.getFC(cbDataRef);
		assertEquals("ST", v);
	}

	@Test
	public void testGetDoName() {
		String v = SclUtil.getDoName(cbDataRef);
		assertEquals("Alm6", v);
		v = SclUtil.getDoName(pinDataRef);
		assertEquals("IntIn3", v);
	}

	@Test
	public void testGetLnName() {
		String v = SclUtil.getLnName(cbDataRef);
		assertEquals("GGIO", v);
		v = SclUtil.getLnName(pinDataRef);
		assertEquals("GGIO", v);
	}

}
