/**
 * Copyright (c) 2007-2017 思源电气股份有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.shrcn.found.common.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2018-6-27
 */
public class CRC12 {
	
	public static int	CRC12(byte[] acbyBuff) {
		return CRC12(acbyBuff, acbyBuff.length);
	}

	public static int	CRC12(byte[] acbyBuff, int iLen)
	{
		int	wCRC;
			int 	i, iQ;
			byte	iR;
			
			wCRC = 0;
			iQ = 0;
			iR = 0;
			while(iQ < iLen)
			{
					// 多项式除法
					// 如果该位为1
					if((acbyBuff[iQ] & (0x80>>iR)) > 0)
					{
						  // 则在余数尾部添1否则添0
					    wCRC |= 0x01;
					}
				
					// 如果12位除数中的最高位为1，则够除
					if(wCRC >= 0x1000)
					{
					    wCRC ^= 0x180D;
					}
						
					wCRC <<= 1;
					iR++;
					if(8 == iR)
					{
							iR = 0;
							iQ++;
					}
		  }
			// 对后面添加的12个0做处理
			for(i=0; i<12; i++)
			{
					if(wCRC >= 0x1000)
					{
					    wCRC ^= 0x180D;
				  }
					wCRC <<= 1;
			}
			wCRC >>= 1;
			return wCRC;
	}
	
	// 查表法CRC12校验码计算函数 
	public static int	TableCRC12(byte[] buff) {
		return TableCRC12(buff, buff.length);
	}
	
	public static int	TableCRC12(byte[] buff, int len)
	{
		int		n;
		int	Cn;
		int	ab, fg;
		
		int 	awCRC_Code[] =
		{
	        0x0000, 0x080D, 0x0817, 0x001A, 0x0823, 0x002E, 0x0034, 0x0839, 
	        0x084B, 0x0046, 0x005C, 0x0851, 0x0068, 0x0865, 0x087F, 0x0072, 
	        0x089B, 0x0096, 0x008C, 0x0881, 0x00B8, 0x08B5, 0x08AF, 0x00A2, 
	        0x00D0, 0x08DD, 0x08C7, 0x00CA, 0x08F3, 0x00FE, 0x00E4, 0x08E9, 
	        0x093B, 0x0136, 0x012C, 0x0921, 0x0118, 0x0915, 0x090F, 0x0102, 
	        0x0170, 0x097D, 0x0967, 0x016A, 0x0953, 0x015E, 0x0144, 0x0949, 
	        0x01A0, 0x09AD, 0x09B7, 0x01BA, 0x0983, 0x018E, 0x0194, 0x0999, 
	        0x09EB, 0x01E6, 0x01FC, 0x09F1, 0x01C8, 0x09C5, 0x09DF, 0x01D2, 
	        0x0A7B, 0x0276, 0x026C, 0x0A61, 0x0258, 0x0A55, 0x0A4F, 0x0242, 
	        0x0230, 0x0A3D, 0x0A27, 0x022A, 0x0A13, 0x021E, 0x0204, 0x0A09, 
	        0x02E0, 0x0AED, 0x0AF7, 0x02FA, 0x0AC3, 0x02CE, 0x02D4, 0x0AD9, 
	        0x0AAB, 0x02A6, 0x02BC, 0x0AB1, 0x0288, 0x0A85, 0x0A9F, 0x0292, 
	        0x0340, 0x0B4D, 0x0B57, 0x035A, 0x0B63, 0x036E, 0x0374, 0x0B79, 
	        0x0B0B, 0x0306, 0x031C, 0x0B11, 0x0328, 0x0B25, 0x0B3F, 0x0332, 
	        0x0BDB, 0x03D6, 0x03CC, 0x0BC1, 0x03F8, 0x0BF5, 0x0BEF, 0x03E2, 
	        0x0390, 0x0B9D, 0x0B87, 0x038A, 0x0BB3, 0x03BE, 0x03A4, 0x0BA9, 
	        0x0CFB, 0x04F6, 0x04EC, 0x0CE1, 0x04D8, 0x0CD5, 0x0CCF, 0x04C2, 
	        0x04B0, 0x0CBD, 0x0CA7, 0x04AA, 0x0C93, 0x049E, 0x0484, 0x0C89, 
	        0x0460, 0x0C6D, 0x0C77, 0x047A, 0x0C43, 0x044E, 0x0454, 0x0C59, 
	        0x0C2B, 0x0426, 0x043C, 0x0C31, 0x0408, 0x0C05, 0x0C1F, 0x0412, 
	        0x05C0, 0x0DCD, 0x0DD7, 0x05DA, 0x0DE3, 0x05EE, 0x05F4, 0x0DF9, 
	        0x0D8B, 0x0586, 0x059C, 0x0D91, 0x05A8, 0x0DA5, 0x0DBF, 0x05B2, 
	        0x0D5B, 0x0556, 0x054C, 0x0D41, 0x0578, 0x0D75, 0x0D6F, 0x0562, 
	        0x0510, 0x0D1D, 0x0D07, 0x050A, 0x0D33, 0x053E, 0x0524, 0x0D29, 
	        0x0680, 0x0E8D, 0x0E97, 0x069A, 0x0EA3, 0x06AE, 0x06B4, 0x0EB9, 
	        0x0ECB, 0x06C6, 0x06DC, 0x0ED1, 0x06E8, 0x0EE5, 0x0EFF, 0x06F2, 
	        0x0E1B, 0x0616, 0x060C, 0x0E01, 0x0638, 0x0E35, 0x0E2F, 0x0622, 
	        0x0650, 0x0E5D, 0x0E47, 0x064A, 0x0E73, 0x067E, 0x0664, 0x0E69, 
	        0x0FBB, 0x07B6, 0x07AC, 0x0FA1, 0x0798, 0x0F95, 0x0F8F, 0x0782, 
	        0x07F0, 0x0FFD, 0x0FE7, 0x07EA, 0x0FD3, 0x07DE, 0x07C4, 0x0FC9, 
	        0x0720, 0x0F2D, 0x0F37, 0x073A, 0x0F03, 0x070E, 0x0714, 0x0F19, 
	        0x0F6B, 0x0766, 0x077C, 0x0F71, 0x0748, 0x0F45, 0x0F5F, 0x0752
		};	
		//	第n个字节Bn求CRC12相当于
		//	Bn 00 00 添12个0对180DH求余 
		//  Bn:xy  00    0
		// 		-----------------
		//		 |(Hi) |(Lo)
		//	Cn:	 | ab  | c
		//  Bn+1 | de  | 0 00
		//		 +-----+---------
		//	B'n	 | fg  | c 00	    B'n = Bn+1^Cn_Hi 查表求得Tn的校验码 C'n = i jk
		//	C'n		     i jk
		//			   ----------	
		//				 l jk
		//
		//	Cn+1 =   ((Cn&0x0F) << 8) ^ C'n
		
			Cn = 0;
			for(n=0; n<len; n++)
			{
					ab	= ((Cn & 0x0FF0) >> 4);
					fg  = (buff[n]^ab);
					Cn = ((Cn&0x000F) << 8) ^ awCRC_Code[fg];
			}
		
			return Cn;
	}
	
	private static String getCRCStr(int crc) {
		String crcStr = ByteUtil.bytes2HexString(ByteUtil.intToBytesBE(crc));
		int len = crcStr.length();
		return crcStr.substring(len-3, len);
	}
	
	static Map<Character, Integer> idx = new HashMap<>();
	
	static {
		int p = 0;
		for (char i='0'; i<='9'; i++) {
			idx.put(i, p);
			p++;
		}
		for (char i='a'; i<='z'; i++) {
			idx.put(i, p);
			p++;
		}
		for (char i='A'; i<='Z'; i++) {
			idx.put(i, p);
			p++;
		}
		idx.put('/', p);
		p++;
		idx.put('$', p);
		p++;
		idx.put('_', p);
	}
	
	
	public static String calc(String s) {
		if (!StringUtil.isEmpty(s)) {
			try {
				return getCRCStr(CRC12(s.getBytes("UTF-8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String table(String s) {
		if (!StringUtil.isEmpty(s)) {
			try {
				return getCRCStr(TableCRC12(s.getBytes("UTF-8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
//		byte[] data = "123456789".getBytes("UTF-8");
//		int a = CRC12(data, data.length);
//		int b = TableCRC12(data, data.length);
//		System.out.println(a == b);
		String a = "123456789";
//		String b = "PLZ2201APIGO/LLN0$GO$gocb1";
//		String c = "PLZ2201AMUSV/LLN0$MS$MSVCB01";
		String b = "PS3502PI/LLN0$GO$gocb0";
		String c = "IL2251ARPIT/LLN0$GO$gocb1";
		System.out.println(a + "\t" + calc(a) + "\t" + table(a));
		System.out.println(b + "\t" + calc(b) + "\t" + table(b));
		System.out.println(c + "\t" + calc(c) + "\t" + table(c));
		
	}
	
}
