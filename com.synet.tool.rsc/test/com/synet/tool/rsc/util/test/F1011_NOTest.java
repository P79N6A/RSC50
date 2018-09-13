package com.synet.tool.rsc.util.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.synet.tool.rsc.util.F1011_NO;

public class F1011_NOTest {

	@Test
	public void testGetType() {
		F1011_NO t1 = F1011_NO.getType("dsRelayEna", "PTRC", "Strp", "压板1");
		F1011_NO t2 = F1011_NO.getType("dsWarning", "", "Alm", "CT1102断线");
		F1011_NO t3 = F1011_NO.getType("dsRelayAin", "MMXU", "", "电流1");
		F1011_NO t4 = F1011_NO.getType("", "SPVT", "", "电源电压");
		assertEquals(F1011_NO.GOOUT_STRAP, t1);//34
		assertEquals(F1011_NO.IED_WRN_CPT, t2);//54
		assertEquals(F1011_NO.PROT_MEAR, t3);//101
		assertEquals(F1011_NO.IED_VOL220, t4);//148
	}

}
