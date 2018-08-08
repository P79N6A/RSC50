/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.shrcn.found.file.util.FileManipulate;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-3
 */
public class FileManipulateTest {

	@Test
	public void testInitDir() {
		fail("尚未实现");
	}

	@Test
	public void testClearDir() {
		fail("尚未实现");
	}

	@Test
	public void testRenameFile() {
		fail("尚未实现");
	}

	@Test
	public void testChangeDirectory() {
		fail("尚未实现");
	}

	@Test
	public void testDeleteFiles() {
		fail("尚未实现");
	}

	@Test
	public void testDeleteFileString() {
		fail("尚未实现");
	}

	@Test
	public void testDeleteFileStringBoolean() {
		fail("尚未实现");
	}

	@Test
	public void testCopyDir() {
		fail("尚未实现");
	}

	@Test
	public void testCopyByChannelStringString() {
		fail("尚未实现");
	}

	@Test
	public void testCopyByChannelFileFile() {
		fail("尚未实现");
	}

	@Test
	public void testCopyDirectiory() {
		fail("尚未实现");
	}

	@Test
	public void testExist() {
		fail("尚未实现");
	}

	@Test
	public void testAppentTextToFile() {
		fail("尚未实现");
	}

	@Test
	public void testMergeFiles() {
		fail("尚未实现");
	}

	@Test
	public void testCopyToInputStreamOutputStreamBoolean() {
		fail("尚未实现");
	}

	@Test
	public void testCopyToInputStreamOutputStream() {
		fail("尚未实现");
	}

	@Test
	public void testCopyResource() {
		fail("尚未实现");
	}

	@Test
	public void testGetFileCRCCodeFile() {
		fail("尚未实现");
	}

	@Test
	public void testGetStreamCRCCode() {
		fail("尚未实现");
	}

	@Test
	public void testGetFileCRCCodeString() {
		fail("尚未实现");
	}

	@Test
	public void testGetFileCRCCodeStringArray() {
		fail("尚未实现");
	}

	@Test
	public void testGetCRC32Clean() {
		fail("尚未实现");
	}
	
	private static final int POLY = 0x04C11DB7;
	
//	// 以4 byte数据为例
//	#define POLY 0x04C11DB7L // CRC32生成多项式
//	unsigned int CRC32_1(unsigned int data)
//	{
//		unsigned char p[8];
//		memset(p, 0, sizeof(p));
//		memcpy(p, &data, 4);
//		unsigned int reg = 0, idx = 0;
//		for(int i = 0; i < 64; i++)
//		{
//			idx = i/8;
//			int hi = (reg>>31)&0x01; // 取得reg的最高位
//			// 把reg左移1bit，并移入新数据到reg0
//			reg = (reg<<1)| (p[idx]>>7);
//			if(hi) reg = reg^POLY; // hi=1就用reg除以g(x)
//			p[idx]<<=1;
//		}
//		return reg;
//	}
	
//	int crc = 0xffffffff;
//	for (byte b : bytes) {
//		crc = (crc >>> 8 ^ table[(crc ^ b) & 0xff]);
//	}
//	crc = crc ^ 0xffffffff;
	
	private int getCRC32(byte[] data) {
		byte[] bs = new byte[data.length * 2];
		int idx = 0;
		for(int i = 0; i < data.length * 2; i++) {
			idx = i/2;
			boolean h = ((i%2) == 0);
			if (h)
				bs[i] = (byte)(data[idx] >> 4);
			else
				bs[i] = (byte)(data[idx] & 0x0F);
		}
		
		int reg = 0; 
//		int reg = 0xffffffff;
		idx = 0;
		for(int i = 0; i < 8; i++)
		{
			idx = i/8;
			int hi = (reg>>31)&0x01; // 取得reg的最高位
			// 把reg左移1bit，并移入新数据到reg0
			reg = (reg<<1)| (bs[idx]>>7);
			if(hi > 0) reg = reg^POLY; // hi=1就用reg除以g(x)
//			p[idx]<<=1;
			bs[idx] = (byte) (bs[idx] << 1);
		}
//		reg = reg ^ 0xffffffff;
		return reg;
	}


	@Test
	public void testGetCRC32() throws IOException {
//		// 符合国网检测要求
		assertEquals("A2B4FD62",
				FileManipulate.getCRC32("123456789abcdef"));
		
//		String path = "E:\\work\\SHR\\SCT\\送检\\CCD-高磊\\2840\\20150722_161039_B6A30D59\\PL2214B.txt";
//		assertEquals("F193A72A",
//				FileManipulate.getCRC32(FileManager.readFile(path)));
//		
//		assertEquals("F193A72A",
//				FileManipulate.getFileCRCCode(path));
		
//		int crc32 = getCRC32("123456789abcdef".getBytes());
//		String crc = Integer.toHexString(crc32).toUpperCase();
//		assertEquals("A2B4FD62", crc);
	}

	@Test
	public void testGetResourceURL() {
		fail("尚未实现");
	}

	@Test
	public void testDeleteDirFile() {
		fail("尚未实现");
	}

	@Test
	public void testDeleteDirString() {
		fail("尚未实现");
	}

	@Test
	public void testGetCRC32Code() {
		fail("尚未实现");
	}

	@Test
	public void testGetMD5Code() {
		fail("尚未实现");
	}

	@Test
	public void testGetMD5CodeForStr() {
		fail("尚未实现");
	}

	@Test
	public void testAddSuffix() {
		fail("尚未实现");
	}

	@Test
	public void testFindFiles() {
		fail("尚未实现");
	}

	@Test
	public void testGetSubFileNames() {
		fail("尚未实现");
	}

	@Test
	public void testGetSubFiles() {
		fail("尚未实现");
	}

	@Test
	public void testGetName() {
		fail("尚未实现");
	}

	@Test
	public void testGetFileName() {
		fail("尚未实现");
	}

	@Test
	public void testGetConfigFile() {
		fail("尚未实现");
	}
	
	// 生成CRC32 普通表 , 第二项是04C11DB7  
	static void gen_direct_table(int[] table)  
	{  
	    int gx = 0x04c11db7;  
	    int i32, j32;  
	    long nData32;  
	    long nAccum32;  
	    for ( i32 = 0; i32 < 256; i32++ )  
	    {  
	        nData32 = (long )( i32 << 24 );  
	        nAccum32 = 0;  
	        for ( j32 = 0; j32 < 8; j32++ )  
	        {  
	            if ( (( nData32 ^ nAccum32 ) & 0x80000000) >0)  
	                nAccum32 = ( nAccum32 << 1 ) ^ gx;  
	            else  
	                nAccum32 <<= 1;  
	            nData32 <<= 1;  
	        }  
	        table[i32] = (int) nAccum32;  
	    }  
	}
	
	// 生成CRC32 翻转表 第二项是77073096  
	static void gen_normal_table(long[] table)  
	{  
	    int gx = 0x04c11db7;  
	    long temp,crc;  
	    for(int i = 0; i <= 0xFF; i++)   
	    {  
	        temp=Reflect(i, 8);  
	        table[i]= temp<< 24;  
	        for (int j = 0; j < 8; j++)  
	        {  
	            long t1,t2;  
	            long flag=table[i]&0x80000000;  
	            t1=(table[i] << 1);  
	            if(flag==0)  
	            t2=0;  
	            else  
	            t2=gx;  
	            table[i] =t1^t2 ;  
	        }  
	        crc=table[i];  
	        table[i] = Reflect(table[i], 32);  
	    }  
	} 
	
	//位翻转函数  
	static long Reflect(long ref, int ch)  
	{     
	    int i;  
	    long value = 0;  
	    for( i = 1; i < ( ch + 1 ); i++ )  
	    {  
	        if( (ref & 1) > 0 )  
	            value |= 1 << ( ch - i );  
	        ref >>= 1;  
	    }  
	    return value;  
	}  
	  
	
	public static void main(String[] args) {
		 int sum = 256;  
		 int i = 0;  
		    
		 //生成普通表，用于直接计算的表
//		int[] Table1 = new int[sum];
//	    gen_direct_table(Table1);  
		long[] Table1 = new long[sum];
		gen_normal_table(Table1);
	    printf("Table1 :\n");  
	    for( i = 0; i < sum; i++)  
	    {  
	        if(i<16)  
	            printf("%08x ", ((int)Table1[i]));  
	    }  
	    printf("\n\n");  
	}
	
	
//	#define POLYNOMIAL_TMP (uint32_t)0xEDB88320
//	static uint32_t aulTbl32_g_TMP[256];
//	
//	uint32_t EP_CRC32_TMP(unsigned char *pucBuf, int uiLen, uint32_t ulCrc)
//	{
//	    if (!aulTbl32_g_TMP[255])
//	        EP_Make_CRC32_Table_TMP();
//		
//	    ulCrc ^= 0xFFFFFFFF;
//	    
//	    while (uiLen--)
//	        ulCrc = (ulCrc >> 8) ^ aulTbl32_g_TMP[(ulCrc ^ *pucBuf++) & 0xFF];
//	    
//	    return ulCrc ^ 0xFFFFFFFF;
//	}
//	
//	static void EP_Make_CRC32_Table_TMP(void)
//	{
//		unsigned int i, j;
//		uint32_t ul = 1;
//		
//	    aulTbl32_g_TMP[0] = 0;
//	    for (i = 128; i; i >>= 1)
//	    {
//	        ul = (ul >> 1) ^ ((ul & 1) ? POLYNOMIAL_TMP : 0);
//			
//	        /* ul is now aulTbl32_g[i] */
//	        for (j = 0; j < 256; j += 2*i)
//	            aulTbl32_g_TMP[i+j] = aulTbl32_g_TMP[j] ^ ul;
//	    }
//	}
	
//	private int POLYNOMIAL_TMP = 0xEDB88320;
//	private int[] aulTbl32_g_TMP = new int[256];
//	
//	int EP_CRC32_TMP(byte[] pucBuf, int uiLen, int ulCrc)
//	{
//	    if (!aulTbl32_g_TMP[255])
//	        EP_Make_CRC32_Table_TMP();
//		
//	    ulCrc ^= 0xFFFFFFFF;
//	    
//	    while (uiLen--)
//	        ulCrc = (ulCrc >> 8) ^ aulTbl32_g_TMP[(ulCrc ^ *pucBuf++) & 0xFF];
//	    
//	    return ulCrc ^ 0xFFFFFFFF;
//	}
//	
//	static void EP_Make_CRC32_Table_TMP(void)
//	{
//		unsigned int i, j;
//		uint32_t ul = 1;
//		
//	    aulTbl32_g_TMP[0] = 0;
//	    for (i = 128; i; i >>= 1)
//	    {
//	        ul = (ul >> 1) ^ ((ul & 1) ? POLYNOMIAL_TMP : 0);
//			
//	        /* ul is now aulTbl32_g[i] */
//	        for (j = 0; j < 256; j += 2*i)
//	            aulTbl32_g_TMP[i+j] = aulTbl32_g_TMP[j] ^ ul;
//	    }
//	}

	private static void printf(String string, long i) {
		System.out.println(String.format(string, i));
	}

	private static void printf(String string) {
		System.out.println(string);
	}

}

