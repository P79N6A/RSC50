/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.common.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.shrcn.found.common.util.ByteUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-2-26
 */
/**
 * $Log$
 */
public class ByteUtilTest {

	@Test
	public void testPrintHexString() {
		fail("尚未实现");
	}

	@Test
	public void testBytes2HexStringByteArrayIntInt() {
		fail("尚未实现");
	}

	@Test
	public void testBytes2HexStringByteArray() {
		fail("尚未实现");
	}

	@Test
	public void testByte2HexString() {
		fail("尚未实现");
	}

	@Test
	public void testToHexStringSpace() {
		fail("尚未实现");
	}

	@Test
	public void testUniteBytes() {
		fail("尚未实现");
	}

	@Test
	public void testHexString2Bytes() {
		fail("尚未实现");
	}

	@Test
	public void testHexStringToByte() {
//		fail("尚未实现");
		String hex = "736872686F6E6772756931323334353637383930313233343536373839303132";
		byte[] data = ByteUtil.hexStringToByte(hex);
		assertTrue(data.length > 0);
		try {
			System.out.println(new String(data, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAddrHexStringToByte() {
		fail("尚未实现");
	}

	@Test
	public void testAddrByteToString() {
		fail("尚未实现");
	}

	@Test
	public void testToByte() {
		fail("尚未实现");
	}

	@Test
	public void testHexStr2Str() {
		fail("尚未实现");
	}

	@Test
	public void testBytesToShortBE() {
		fail("尚未实现");
	}

	@Test
	public void testShortToBytesBE() {
		fail("尚未实现");
	}

	@Test
	public void testBytesToShort() {
		fail("尚未实现");
	}

	@Test
	public void testShortToBytes() {
		fail("尚未实现");
	}

	@Test
	public void testBytesToIntBE() {
		fail("尚未实现");
	}

	@Test
	public void testIntToBytesBE() {
		fail("尚未实现");
	}

	@Test
	public void testBytesToInt() {
		fail("尚未实现");
	}

	@Test
	public void testIntToBytesInt() {
		int num = 1009;
		byte[] buf = ByteUtil.intToBytes(num);
		assertEquals(num, ByteUtil.bytesToInt(buf, 0));
	}

	@Test
	public void testBytesToLongBE() {
		fail("尚未实现");
	}

	@Test
	public void testLongToBytes() {
		fail("尚未实现");
	}

	@Test
	public void testBytesToFloat() {
		fail("尚未实现");
	}

	@Test
	public void testFloatToBytes() {
		fail("尚未实现");
	}

	@Test
	public void testBytesToBoolean() {
		fail("尚未实现");
	}

	@Test
	public void testBuildNumericBufferByteArrayIntInt() {
		fail("尚未实现");
	}

	@Test
	public void testBuildNumericBufferByteArrayIntShort() {
		fail("尚未实现");
	}

	@Test
	public void testBuildNumericBufferByteArrayIntFloat() {
		fail("尚未实现");
	}

	@Test
	public void testBuildNumericBufferByteArrayIntByte() {
		fail("尚未实现");
	}

	@Test
	public void testByteToInt() {
		fail("尚未实现");
	}

	@Test
	public void testGetDouble() {
		fail("尚未实现");
	}

	@Test
	public void testGetFloat() {
		fail("尚未实现");
	}

	@Test
	public void testGetLong() {
		fail("尚未实现");
	}

	@Test
	public void testGetUnsignedByteByte() {
		fail("尚未实现");
	}

	@Test
	public void testGetUnsignedByteShort() {
		fail("尚未实现");
	}

	@Test
	public void testGetUnsignedIntt() {
		fail("尚未实现");
	}

	@Test
	public void testSetWordByteBufferIntInt() {
		fail("尚未实现");
	}

	@Test
	public void testSetWordByteBufferIntIntBoolean() {
		fail("尚未实现");
	}

	@Test
	public void testPutWord() {
		fail("尚未实现");
	}

	@Test
	public void testSetWordByteBufferIntBoolean() {
		fail("尚未实现");
	}

	@Test
	public void testBoolStrToInt() {
		fail("尚未实现");
	}

	@Test
	public void testIntToBool() {
		fail("尚未实现");
	}

	@Test
	public void testStrToByte() {
		fail("尚未实现");
	}

	@Test
	public void testIntToBytesIntIntBoolean() {
		fail("尚未实现");
	}

	@Test
	public void testMergeBytes() {
		byte[] arr1 = ByteUtil.hexString2Bytes("2e3dd3");
		byte[] arr2 = ByteUtil.hexString2Bytes("eeddff");
		byte[] data = ByteUtil.mergeBytes(arr1, arr2);
		assertEquals("2e 3d d3 ee dd ff ", ByteUtil.toHexStringSpace(data));
	}

	@Test
	public void testGetMacString() {
		String mac = "00-26-9E-B1-36-A2";
		byte[] macData = ByteUtil.getMacBytes(mac);
		assertEquals(mac, ByteUtil.getMacString(macData));
	}
	
}
