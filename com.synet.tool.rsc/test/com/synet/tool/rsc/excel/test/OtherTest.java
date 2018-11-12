package com.synet.tool.rsc.excel.test;

import org.junit.Test;

public class OtherTest {

	@Test
	public void test(){
		char A = 'A';
		for (int i = 0; i < 5; i++) {
			char c = (char) (A + i);
			System.out.println("" + c);
		}
	}
}
