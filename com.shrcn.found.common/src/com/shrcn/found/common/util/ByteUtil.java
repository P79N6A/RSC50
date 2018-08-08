/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.commons.lang.ArrayUtils;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-11-3
 */
/**
 * $Log: ByteUtil.java,v $ Revision 1.4 2013/05/23 06:30:04 cchun
 * Update:添加toHexStringSpace()
 * 
 * Revision 1.3 2013/05/16 00:39:42 cchun Update:添加合并单元板卡参数处理方法
 * 
 * Revision 1.2 2013/05/07 01:31:48 cchun Update:增加bytes2HexString()不同参数方法
 * 
 * Revision 1.1 2013/03/29 09:36:29 cchun Add:创建
 * 
 * Revision 1.1 2010/11/03 08:28:09 cchun Add:字节处理工具类
 * 
 */
public class ByteUtil {

	public static final int MAC_LEN = 6;

	/**
	 * 将指定byte数组以16进制的形式打印到控制台
	 * 
	 * @param hint
	 *            String 提示信息
	 * @param b
	 *            byte[] 待打印字节数组
	 * @return void
	 */
	public static void printHexString(String hint, byte[] b) {
		System.out.print(hint);
		System.out.print(toHexStringSpace(b));
		System.out.println("");
	}

	/**
	 * 将字节数组转换成16进制字符串
	 * 
	 * @param b
	 * @param offset
	 * @param len
	 * @return
	 */
	public static String bytes2HexString(byte[] b, int offset, int len) {
		String ret = "";
		for (int i = offset; i < offset + len; i++) {
			String hex = byte2HexString(b[i]);
			ret += hex;
		}
		return ret;
	}

	/**
	 * 将字节数组转换成16进制字符串
	 * 
	 * @param b
	 *            待转换字节数组
	 * @return
	 */
	public static String bytes2HexString(byte[] b) {
		return bytes2HexString(b, 0, b.length);
	}

	/**
	 * 将字节转成16进制格式
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2HexString(byte b) {
		String hex = Integer.toHexString(b & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		return hex.toUpperCase();
	}

	/**
	 * Byte转Bit 返回2进制字符串
	 */
	public static String byteToBit(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
				+ (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
				+ (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
				+ (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
	}

	/**
	 * 数组转成十六进制字符串，顺序转换，每字节间加空格
	 * 
	 * @param byte[]
	 * @return HexString
	 */
	public static String toHexStringSpace(byte[] b) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < b.length; ++i) {
			buffer.append(byte2HexString(b[i]));
			buffer.append(" ");
		}
		return buffer.toString();
	}
	
	/**
	 * 数组转成十进制字符串，顺序转换，每字节间加空格
	 * 
	 * @param byte[]
	 * @return HexString
	 */
	public static String toIntegerStringSpace(byte[] b) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < b.length; ++i) {
//			int  tempInt= (int) (byte) (b[i] & 0xff);
			buffer.append((b[i] & 0xff));
			buffer.append(" ");
		}
		return buffer.toString();
	}

	/**
	 * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
	 * 
	 * @param src0
	 *            byte
	 * @param src1
	 *            byte
	 * @return byte
	 */
	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	/**
	 * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF,
	 * 0xD9}
	 * 
	 * @param src
	 *            String
	 * @return byte[]
	 */
	public static byte[] hexString2Bytes(String src) {
		int strLen = src.length();
		if ((strLen % 2) != 0)
			return null;
		int retLen = strLen / 2;
		byte[] ret = new byte[retLen];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < retLen; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

	/**
	 * 把16进制字符串转换成字节数组，顺序反转转换 public static byte[] HexStringTo4Byte(String hex)
	 * 
	 * @param hexString
	 * @return byte[]
	 */
	public static byte[] hexStringToByte(String hex) {
		hex = hex.toUpperCase();
		if ((hex.length() % 2) != 0)
			hex = "0" + hex;
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		int j = len - 1;
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[j] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
			j--;
		}
		return result;
	}

	/**
	 * 把16进制字符串转换成字节数组，顺序2个16进制字符转换成1个字节 public static byte[]
	 * hexStringToByte(String hex)
	 * 
	 * @param hexString
	 * @return byte[]
	 */
	public static byte[] addrHexStringToByte(String hex) {
		if ((hex.length() % 2) != 0)
			hex = "0" + hex;
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	/**
	 * 把地址字节数据转换成地址字符串，顺序反转转换 public static String FourByteToString(byte[]
	 * lbyte)
	 * 
	 */
	public static String addrByteToString(byte[] lbyte) {
		int len = lbyte.length;

		String result = "";
		String temp = "";
		for (int i = 0; i < len; i++) {
			temp = byte2HexString(lbyte[len - i - 1]);
			result = result + temp;
		}
		return result;
	}

	public static int toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * 十六进制字符串转换成字符串
	 * 
	 * @param hexString
	 * @return String
	 */
	public static String hexStr2Str(String hexStr) {
		if ((hexStr.length() % 2) != 0)
			hexStr = "0" + hexStr;
		hexStr = hexStr.toUpperCase();
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}

	/**
	 * 将字节数组中指定的2个字节转换成short(高位在前)
	 * 
	 * @param bs
	 *            源字节数组
	 * @param offset
	 *            起始位置
	 * @return
	 */
	public static short bytesToShortBE(byte[] bs, int offset) {
		short res = 0;
		for (int i = 0; i < 2; i++) {
			res <<= 8;
			res |= (bs[i + offset] & 0xff);
		}
		return res;
	}
	
	//hanhouyang add
	/**
	 * 将字节数组中指定的len个字节转换成字符串
	 * @param bs
	 * @param offset
	 * @return
	 */
	public static String bytesToStr(byte[] bs, int offset,int len) {
		byte[] bytes = new byte[len];
		System.arraycopy(bs, offset, bytes, 0, len);
		String res =" ";
		byte[] strbytes = new byte[len];
			System.arraycopy(bytes, 0, strbytes, 0, len);
			try {
				res=new String(strbytes, "utf-8").trim();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		return res;
	} 
	/**
	 * 将short转换成2个字节的数组(高位在前)
	 * 
	 * @param num
	 *            short
	 * @return
	 */
	public static byte[] shortToBytesBE(short num) {
		byte[] b = new byte[2];
		b[0] = (byte) (num >>> 8);
		b[1] = (byte) num;
		return b;
	}

	/**
	 * 将字节数组中指定的2个字节转换成short(低位在前)
	 * 
	 * @param bs
	 *            源字节数组
	 * @param offset
	 *            起始位置
	 * @return
	 */
	public static short bytesToShort(byte[] bs, int offset) {
		short res = 0;
		for (int i = 1; i > -1; i--) {
			res <<= 8;
			res |= (bs[i + offset] & 0xff);
		}
		return res;
	}

	/**
	 * 将short转换成2个字节的数组(低位在前)
	 * 
	 * @param num
	 *            short
	 * @return
	 */
	public static byte[] shortToBytes(short num) {
		byte[] b = new byte[2];
		b[0] = (byte) num;
		b[1] = (byte) (num >>> 8);
		return b;
	}

	/**
	 * 将字节数组中指定的4个字节转换成int(高位在前)
	 * 
	 * @param bs
	 * @param offset
	 * @return
	 */
	public static int bytesToIntBE(byte[] bs, int offset) {
		int res = 0;
		for (int i = 0; i < 4; i++) {
			res <<= 8;
			res |= (bs[i + offset] & 0xff);
		}
		return res;
	}

	/**
	 * int类型转换成byte[]（高位在前）
	 * 
	 * @param num
	 *            int数
	 * @return byte[]
	 */
	public static byte[] intToBytesBE(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}
	
	/**
	 * uint16类型转换成byte[]（高位在前）
	 * 
	 * @param num
	 *            int数
	 * @return byte[]
	 */
	public static byte[] uint16ToBytes(int num) {
		byte[] b = new byte[2];
		for (int i = 0; i < 2; i++) {
			b[i] = (byte) (num >>> (8 - i * 8));
		}
		return b;
	}
	/**
	 * uint类型转换成byte[]（高位在前）
	 * 
	 * @param num
	 *            int数
	 * @return byte[]
	 */
	public static byte[] uintToBytesBE(long num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}

	/**
	 * 将字节数组中指定的4个字节转换成int(低位在前)
	 * 
	 * @param bs
	 *            源字节数组
	 * @param offset
	 *            起始位置
	 * @return
	 */
	public static int bytesToInt(byte[] bs, int offset) {
		int res = 0;
		for (int i = 3; i > -1; i--) {
			res <<= 8;
			res |= (bs[i + offset] & 0xff);
		}
		return res;
	}
	/**
	 * 将字节数组中指定的4个字节转换成unginedint32
	 * @param bs
	 * @param offset
	 * @return
	 */
	public static long bytesToUInt(byte[] bs, int offset) {
		int res = 0;
		for (int i = 3; i > -1; i--) {
			res <<= 8;
			res |= (bs[i + offset] & 0xff);
		}
		return res & 0x0FFFFFFFFl;

	}
	public static long bytesToUIntBE(byte[] bs, int offset) {
		int res = 0;
		for (int i = 0; i < 4; i++) {
			res <<= 8;
			res |= (bs[i + offset] & 0xff);
		}
		return res & 0x0FFFFFFFFl;
	}
	//hanhouyang add
	/**
	 * 将字节数组中指定的4个字节转换成unginedint16
	 * @param bs
	 * @param offset
	 * @return
	 */
	public static int bytesToUNInt16(byte[] bs, int offset) {
		int res = 0;
		for (int i = 1; i > -1; i--) {
			res <<= 8;
			res |= (bs[i + offset] & 0xff);
		}
		return  res & 0x0FFFF;
	}
	
	//hanhouyang add
	/**
	 * 将字节数组中指定的4个字节转换成unginedint16
	 * @param bs
	 * @param offset
	 * @return
	 */
	public static int bytesToUNInt16BE(byte[] bs, int offset) {
		int res = 0;
		for (int i = 0; i < 2; i++) {
			res <<= 8;
			res |= (bs[i + offset] & 0xff);
		}
		return  res & 0x0FFFF;
	}
	
	
	
	public static long bytesToUShortBE(byte[] bs, int offset) {
		int res = 0;
		for (int i = 0; i < 2; i++) {
			res <<= 8;
			res |= (bs[i + offset] & 0xff);
		}
		return res & 0x0FFFFFFFFl;

	}
	/**
	 * 将int转换成4个字节的数组(低位在前)
	 * 
	 * @param num
	 *            int
	 * @return
	 */
	public static byte[] intToBytes(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[3 - i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}

	/**
	 * 将字节数组中指定的8个字节转换成long(高位在前)
	 * 
	 * @param bs
	 * @param offset
	 * @return
	 */
	public static long bytesToLongBE(byte[] bs, int offset) {
		long res = 0;
		for (int i = 0; i < 8; i++) {
			res <<= 8;
			res |= (bs[i + offset] & 0xff);
		}
		return res;
	}

	/**
	 * 将字节数组中指定的8个字节转换成long(低位在前)
	 * 
	 * @param bs
	 * @param offset
	 * @return
	 */
	public static long bytesToLong(byte[] bs, int offset) {
		long res = 0;
		for (int i = 7; i > -1; i--) {
			res <<= 8;
			res |= (bs[i + offset] & 0xff);
		}
		return res;
	}

	public static long bytesToULon(byte[] bs, int offset) {
		int res = 0;
		for (int i = 7; i > -1; i--) {
			res <<= 8;
			res |= (bs[i + offset] & 0xff);
		}
		return res & 0x0FFFFFFFFl;

	}
	
	public static long bytesToULonBE(byte[] bs, int offset) {
		int res = 0;
		for (int i = 0; i < 8; i++) {
			res <<= 8;
			res |= (bs[i + offset] & 0xff);
		}
		return res & 0x0FFFFFFFFl;

	}
	
	/**
	 * long类型转换成byte[]
	 * 
	 * @param num
	 *            long数
	 * @return byte[]
	 */
	public static byte[] longToBytes(long num) {
		byte[] b = new byte[8];
		for (int i = 0; i < 8; i++) {
			b[i] = (byte) (num >>> (56 - i * 8));
		}
		return b;
	}
	
	/**
	 * long类型转换成byte[]
	 * 
	 * @param num
	 *            long数
	 * @return byte[]
	 */
	public static byte[] longToBytesBE(long num) {
		byte[] b = new byte[8];
		for (int i = 7; i > -1; i--) {
			b[i] = (byte) (num >>> (56 - i * 8));
		}
		return b;
	}

	/**
	 * 将字节数组转换成float(高位在前)
	 * 
	 * @param bs
	 *            源字节数组
	 * @param offset
	 *            起始位置
	 * @return
	 */
	public static float bytesToFloatBE(byte[] bs, int offset) {
		return Float.intBitsToFloat(bytesToIntBE(bs, offset));
	}
	
	public static float bytesToUFloatBE(byte[] bs, int offset) {
		return Float.intBitsToFloat((int) bytesToUIntBE(bs, offset));
	}

	/**
	 * 将字节数组转换成float(低位在前)
	 * 
	 * @param bs
	 *            源字节数组
	 * @param offset
	 *            起始位置
	 * @return
	 */
	public static float bytesToFloat(byte[] bs, int offset) {
		return Float.intBitsToFloat(bytesToInt(bs, offset));
	}

	/**
	 * 将float转化为字节数组(高位在前)
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] floatToBytesBE(float num) {
		return intToBytesBE(Float.floatToRawIntBits(num));
	}

	/**
	 * 将float转化为字节数组（地位在前）
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] floatToBytes(float num) {
		return intToBytes(Float.floatToRawIntBits(num));
	}

	public static byte[] doubleToBytes(double num) {
		return longToBytes(Double.doubleToRawLongBits(num));
	}

	/**
	 * 将字节数组转成boolean值，只取最低一位．
	 * 
	 * @param bs
	 *            源字节数组
	 * @param offset
	 *            起始位置
	 * @return
	 */
	public static boolean bytesToBoolean(byte[] bs, int offset) {
		return bs[offset] != 0;
	}

	public static void buildNumericBuffer(byte[] buf, int offset, int v) {
		byte[] b = intToBytes(v);
		assert (buf.length >= offset + b.length);
		System.arraycopy(b, 0, buf, offset, b.length);
	}

	public static void buildNumericBuffer(byte[] buf, int offset, short v) {
		byte[] b = shortToBytes(v);
		assert (buf.length >= offset + b.length);
		System.arraycopy(b, 0, buf, offset, b.length);
	}

	public static void buildNumericBuffer(byte[] buf, int offset, float v) {
		byte[] b = floatToBytes(v);
		assert (buf.length >= offset + b.length);
		System.arraycopy(b, 0, buf, offset, b.length);
	}

	public static void buildNumericBuffer(byte[] buf, int offset, byte v) {
		assert (buf.length >= offset + 1);
		buf[offset] = v;
	}

	public static int byteToInt(byte[] convertByteValue) {
		int mask = 0xff;
		int temp = 0;
		int res = 0;
		byte[] t = new byte[4];
		t[0] = 0;
		t[1] = 0;
		t[2] = 0;
		t[3] = 0;
		convertByteValue = ArrayUtils.subarray(
				ArrayUtils.addAll(convertByteValue, t), 0, 4);
		// System.out.println(convertByteValue.length);
		for (int i = 3; i > -1; i--) {
			res <<= 8;
			temp = convertByteValue[i + 0] & mask;
			res |= temp;
		}
		return res;

	}

	/**
	 * 通过byte数组取得float
	 * 
	 * @param bb
	 * @return
	 */
	public static double getDouble(byte[] b) {
		byte[] t = new byte[8];
		t[0] = 0;
		t[1] = 0;
		t[2] = 0;
		t[3] = 0;
		t[4] = 0;
		t[5] = 0;
		t[6] = 0;
		t[7] = 0;
		b = ArrayUtils.addAll(b, t);
		b = ArrayUtils.subarray(b, 0, 8);
		long l;
		l = b[0];
		l &= 0xff;
		l |= ((long) b[1] << 8);
		l &= 0xffff;
		l |= ((long) b[2] << 16);
		l &= 0xffffff;
		l |= ((long) b[3] << 24);
		l &= 0xffffffffl;
		l |= ((long) b[4] << 32);
		l &= 0xffffffffffl;
		l |= ((long) b[5] << 40);
		l &= 0xffffffffffffl;
		l |= ((long) b[6] << 48);
		l &= 0xffffffffffffffl;
		l |= ((long) b[7] << 56);
		return Double.longBitsToDouble(l);
	}

	/**
	 * 通过byte数组取得float
	 * 
	 * @param bb
	 * @return
	 */
	public static double getFloat(byte[] b) {
		byte[] t = new byte[8];
		t[0] = 0;
		t[1] = 0;
		t[2] = 0;
		t[3] = 0;

		b = ArrayUtils.addAll(b, t);
		b = ArrayUtils.subarray(b, 0, 4);
		long l;
		l = b[0];
		l &= 0xff;
		l |= ((long) b[1] << 8);
		l &= 0xffff;
		l |= ((long) b[2] << 16);
		l &= 0xffffff;
		l |= ((long) b[3] << 24);
		l &= 0xffffffffl;
		return Double.longBitsToDouble(l);
	}

	public static long getLong(byte[] b) {
		byte[] t = new byte[8];
		t[0] = 0;
		t[1] = 0;
		t[2] = 0;
		t[3] = 0;
		t[4] = 0;
		t[5] = 0;
		t[6] = 0;
		t[7] = 0;
		b = ArrayUtils.addAll(b, t);
		b = ArrayUtils.subarray(b, 0, 8);
		long l;
		l = b[0];
		l &= 0xff;
		l |= ((long) b[1] << 8);
		l &= 0xffff;
		l |= ((long) b[2] << 16);
		l &= 0xffffff;
		l |= ((long) b[3] << 24);
		l &= 0xffffffffl;
		l |= ((long) b[4] << 32);
		l &= 0xffffffffffl;
		l |= ((long) b[5] << 40);
		l &= 0xffffffffffffl;
		l |= ((long) b[6] << 48);
		l &= 0xffffffffffffffl;
		l |= ((long) b[7] << 56);
		return l;
	}

	public static int getUnsignedByte(byte data) { // 将data字节型数据转换为0~255 (0xFF
		return data & 0x0FF;
	}

	public static int getUnsignedByte(short data) { // 将data字节型数据转换为0~65535
													// (0xFFFF 即
		// WORD)。
		return data & 0x0FFFF;
	}

	public static long getUnsignedIntt(int data) { // 将int数据转换为0~4294967295
		// (0xFFFFFFFF即DWORD)。
		return data & 0x0FFFFFFFFl;
	}

	public static void setWord(ByteBuffer buff, int value, int index) {
		byte[] intToBytes = intToBytes(value, 2, true);
		buff.put(index, intToBytes[0]);
		buff.put(index + 1, intToBytes[1]);
	}

	public static void setWord(ByteBuffer buff, int value, int index,
			boolean isBig) {
		byte[] intToBytes = intToBytes(value, 2, isBig);
		buff.put(index, intToBytes[0]);
		buff.put(index + 1, intToBytes[1]);
	}

	public static void putWord(ByteBuffer buff) {
		buff.putShort((short) 0);
	}

	public static void setWord(ByteBuffer buff, int word, boolean isBig) {
		buff.put(intToBytes(word, 2, isBig));
	}

	public static byte boolStrToInt(boolean bool) {
		return (byte) (bool ? 1 : 0);
	}

	public static boolean intToBool(int i) {
		return i == 1;
	}

	public static byte[] strToByte(String str, int size) {
		str = StringUtil.nullToEmpty(str);
		byte[] strBytes = str.getBytes();
		int len = strBytes.length;
		byte[] bytes = new byte[size];

		for (int i = 0; i < size; i++) {
			bytes[i] = ((i < len) ? strBytes[i] : 0);
		}
		return bytes;
	}

	public static byte[] intToBytes(int n, int size, boolean isBig) {
		byte[] buf = new byte[size];
		for (int i = 0; i < size; i++)
			buf[i] = (byte) (n >> 8 * (isBig ? i : (size - i - 1)));
		return buf;
	}

	public static byte[] mergeBytes(byte[] arr1, byte[] arr2) {
		byte[] data = new byte[arr1.length + arr2.length];
		System.arraycopy(arr1, 0, data, 0, arr1.length);
		System.arraycopy(arr2, 0, data, arr1.length, arr2.length);
		return data;
	}

	public static boolean notEmpty(byte[] data) {
		return data != null && data.length > 0;
	}

	/**
	 * 将byte数组转成MAC
	 * 
	 * @param mac
	 * @return
	 */
	public static String getMacString(byte[] mac) {
		if (mac.length < MAC_LEN)
			return null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < MAC_LEN; i++) {
			sb.append(byte2HexString(mac[i]).toUpperCase());
			if (i < MAC_LEN - 1)
				sb.append("-");
		}
		return sb.toString();
	}

	/**
	 * 得到MAC地址字节码。
	 * 
	 * @param macAddr
	 * @return
	 */
	public static byte[] getMacBytes(String macAddr) {
		String[] macs = macAddr.split("-");
		if (macs.length < MAC_LEN)
			return null;
		ByteBuffer buff = ByteBufferUtil.getBuffer(MAC_LEN);
		for (String mac : macs) {
			buff.put((byte) Integer.parseInt(mac, 16));
		}
		return ByteBufferUtil.getData(buff);
	}

	/**
	 * 将byte数组转成IP
	 * 
	 * @param ethd
	 * @return
	 */
	public static String getIPString(byte[] ethd, int offset) {
		if (ethd.length < offset + 4)
			return null;
		StringBuilder sb = new StringBuilder();
		for (int i = offset; i < offset + 4; i++) {
			sb.append((ethd[i] & 0xFF));
			if (i < offset + 3)
				sb.append(".");
		}
		return sb.toString();
	}

	/**
	 * 得到MAC地址字节码。
	 * 
	 * @param ipAddr
	 * @return
	 */
	public static byte[] getIPBytes(String ipAddr) {
		String[] ips = ipAddr.split("\\.");
		if (ips.length < 4)
			return null;
		ByteBuffer buff = ByteBufferUtil.getBuffer(4);
		for (String u : ips) {
			buff.put((byte) Integer.parseInt(u, 16));
		}
		return ByteBufferUtil.getData(buff);
	}

	public static Calendar bytes2Calendar(byte[] bytes) {
		if (bytes.length < 6)
			return null;
		// Fix Bug:解决日期数据较大时解析出错，例如：2042-03-05 16:45:32.990
		long time = (((long) bytes[3] & 0xFF) << 24)
				| (((long) bytes[2] & 0xFF) << 16)
				| (((long) bytes[1] & 0xFF) << 8) | ((long) bytes[0] & 0xFF);
		long mil = bytes[4] & 0xFF | (bytes[5] << 8) & 0xFF03;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis((time * 1000L) + mil);
		return calendar;
	}

	/**
	 * 解析合并单元时间（UTC）
	 * 
	 * @param bytes
	 * @return
	 */
	public static Timestamp bytes2Timestamp(byte[] bytes) {
		if (bytes.length < 12)
			return null;
		int year = (bytes[0] & 0xFF) | (bytes[1] << 8);
		int month = (bytes[2] & 0xFF) | (bytes[3] << 8);
		int day = (bytes[4] & 0xFF) | (bytes[5] << 8);
		int hour = (bytes[6] & 0xFF) | (bytes[7] << 8);
		int min = (bytes[8] & 0xFF) | (bytes[9] << 8);
		int sec = (bytes[10] & 0xFF) | (bytes[11] << 8);
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day, hour + 8, min, sec);
		return new Timestamp(calendar.getTime().getTime());
	}
	
	public static byte bitStr2byte(String bString){
		byte result=0;
		for(int i=bString.length()-1,j=0;i>=0;i--,j++){
		result+=(Byte.parseByte(bString.charAt(i)+"")*Math.pow(2, j));
		}
		return result;
		}
	
	/**
	 * hanhouyang
	 * 将字符串转换为字节数组
	 * @return
	 */
	public static byte[] strToByteBE(String str) {
		byte[] bs=str.getBytes();
		return bs;
	}

//   public static void main(String[] args){		      
//	 String[] city = new String[] { "北京", "上海", "天津", "无锡", "嘉定" }; 
//	 StringBuilder sb = new StringBuilder(); 
//	 for (int i = 0; i < city.length; i++) { 
//		 sb.append(city[i]);
////				 if(i != (city.length-1)) 
////					 sb.append(","); 
//		 } 
//	 System.out.print(sb.toString);		
//   	}
	
//	public static void main(String[] args) {
//		String str="1243437";
//		byte[] bs=str.getBytes();
//		System.out.println(bs);
//	}
}