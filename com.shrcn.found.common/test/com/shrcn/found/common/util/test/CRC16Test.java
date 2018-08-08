package com.shrcn.found.common.util.test;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import com.shrcn.found.common.util.CRC16;

public class CRC16Test {

	@Test
	public void testGetCrcByteArrayInt() {
			String s = "123456789";
			short crc2 = CRC16.getCrc(s.getBytes(), s.length());
			System.out.println(Integer.toHexString(crc2));
	}
	
	private static String getCrc(byte[] data) {  
        int high;  
        int flag;  
  
        // 16位寄存器，所有数位均为1  
        int wcrc = 0xffff;  
        for (int i = 0; i < data.length; i++) {  
            // 16 位寄存器的高位字节  
            high = wcrc >> 8;  
            // 取被校验串的一个字节与 16 位寄存器的高位字节进行“异或”运算  
            wcrc = high ^ data[i];  
  
            for (int j = 0; j < 8; j++) {  
                flag = wcrc & 0x0001;  
                // 把这个 16 寄存器向右移一位  
                wcrc = wcrc >> 1;  
                // 若向右(标记位)移出的数位是 1,则生成多项式 1010 0000 0000 0001 和这个寄存器进行“异或”运算  
                if (flag == 1)  
                    wcrc ^= 0xa001;  
            }  
        }  
  
        return Integer.toHexString(wcrc);  
	}

	/**
	 * 计算CRC16校验码
	 * 
	 * @param bytes
	 *            字节数组
	 * @return {@link String} 校验码
	 * @since 1.0
	 */
	public static String getCRC(byte[] bytes) {
		int CRC = 0x0000ffff;
		int POLYNOMIAL = 0x0000a001;
		int i, j;
		for (i = 0; i < bytes.length; i++) {
			CRC ^= ((int) bytes[i] & 0x000000ff);
			for (j = 0; j < 8; j++) {
				if ((CRC & 0x00000001) != 0) {
					CRC >>= 1;
					CRC ^= POLYNOMIAL;
				} else {
					CRC >>= 1;
				}
			}
		}
		return Integer.toHexString(CRC);
	}

	/**
	 * 将16进制单精度浮点型转换为10进制浮点型
	 * 
	 * @return float
	 * @since 1.0
	 */
	private float parseHex2Float(String hexStr) {
		BigInteger bigInteger = new BigInteger(hexStr, 16);
		return Float.intBitsToFloat(bigInteger.intValue());
	}

	/**
	 * 将十进制浮点型转换为十六进制浮点型
	 * 
	 * @return String
	 * @since 1.0
	 */
	private String parseFloat2Hex(float data) {
		return Integer.toHexString(Float.floatToIntBits(data));
	}
	
	public static void main(String[] args) {
		String s = "123456789";
		System.out.println(getCRC(s.getBytes()));
	}

}
