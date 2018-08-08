package com.shrcn.found.common.util;

import java.io.UnsupportedEncodingException;

/**
 * 系统二进制验证CRC16计算
 * @author xiaohuang
 * @version 1.1
 * @since 2009.8.11 初步完成
 * @since 2010.4.12 增加CRC初值构造
 */
public class CRC16 {
	public CRC16() {}
	public CRC16(int init) {
		crc = init;
	}
	
	private int crc;
	final private int genpoly = 0xa001;
	/**
	 * 获取CRC计算结果
	 */
	public int getValue() {
		return crc & 0xffff;
	}
	/**
	 * 累积计算传入字节的CRC
	 * @param b 一个字节的数据
	 */
	public void update(int b) {
		crc = update(crc, b);
	}
	/**
	 * 累积计算传入的字节数组的CRC
	 * @param bs 要进行累积计算的数据源，不能为null
	 * @param off 开始计算的偏移位置，必须>=0
	 * @param len 从起始偏移开始，计算数据的长度，len+off <= bs.length
	 */
	public void update(byte[] bs, int off, int len) {
		int size = len + off;
		for(int i=off; i<size; ++i)
			update(bs[i]);
	}
	/**
	 * 计算累积计算CRC的实际算法
	 * @param cs 上一次crc值或crc初值
	 * @param b 要计算的字节
	 * @return 新计算的crc
	 */
	private int update(int cs, int b) {
		cs = (((cs^b) & 0x01) == 0x01) ? (cs >>> 1) ^ genpoly : cs >>> 1;
		b >>>= 1;
		cs = (((cs^b) & 0x01) == 0x01) ? (cs >>> 1) ^ genpoly : cs >>> 1;
		b >>>= 1;
		cs = (((cs^b) & 0x01) == 0x01) ? (cs >>> 1) ^ genpoly : cs >>> 1;
		b >>>= 1;
		cs = (((cs^b) & 0x01) == 0x01) ? (cs >>> 1) ^ genpoly : cs >>> 1;
		b >>>= 1;
		
		cs = (((cs^b) & 0x01) == 0x01) ? (cs >>> 1) ^ genpoly : cs >>> 1;
		b >>>= 1;
		cs = (((cs^b) & 0x01) == 0x01) ? (cs >>> 1) ^ genpoly : cs >>> 1;
		b >>>= 1;
		cs = (((cs^b) & 0x01) == 0x01) ? (cs >>> 1) ^ genpoly : cs >>> 1;
		b >>>= 1;
		cs = (((cs^b) & 0x01) == 0x01) ? (cs >>> 1) ^ genpoly : cs >>> 1;

		return cs;
	}
	
	public static short getCrc(byte[] data, int len) {
		CRC16 crc16 = new CRC16();
    	crc16.update(data, 0, len);
    	return (short) crc16.getValue();
	}
	
	public static short getCrc(byte[] data, int offSet, int len) {
		CRC16 crc16 = new CRC16();
    	crc16.update(data, offSet, len);
    	return (short) crc16.getValue();
	}
	
	public static String getCrc(String s) {
		if (!StringUtil.isEmpty(s)) {
			try {
				byte[] data = s.getBytes("UTF-8");
				short crc1 = getCrc(data, data.length);
				return ByteUtil.bytes2HexString(ByteUtil.intToBytesBE(crc1));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
