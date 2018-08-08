package com.shrcn.found.file.util;

/**
 * 系统32位CRC算法，采用以太网帧校验CRC
 * @author xiaohuang
 * @since 2010.2.27 初步完成
 */
public class CRC32 {
	public CRC32() {}
	public CRC32(int init) {
		crc = init;
	}
	
	private int crc = 0xffffffff;
	private final int genpoly = 0x04c11db7;
	
	/**
	 * 获取CRC计算结果
	 */
	public int getValue() {
		return crc;
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

		/*cs = ((cs & 0x80000000) != 0) ? (cs << 1) ^ genpoly : cs << 1;
		if((b & 0x01) != 0) cs ^= genpoly;
		cs = ((cs & 0x80000000) != 0) ? (cs << 1) ^ genpoly : cs << 1;
		if((b & 0x02) != 0) cs ^= genpoly;
		cs = ((cs & 0x80000000) != 0) ? (cs << 1) ^ genpoly : cs << 1;
		if((b & 0x04) != 0) cs ^= genpoly;
		cs = ((cs & 0x80000000) != 0) ? (cs << 1) ^ genpoly : cs << 1;
		if((b & 0x08) != 0) cs ^= genpoly;
		
		cs = ((cs & 0x80000000) != 0) ? (cs << 1) ^ genpoly : cs << 1;
		if((b & 0x10) != 0) cs ^= genpoly;
		cs = ((cs & 0x80000000) != 0) ? (cs << 1) ^ genpoly : cs << 1;
		if((b & 0x20) != 0) cs ^= genpoly;
		cs = ((cs & 0x80000000) != 0) ? (cs << 1) ^ genpoly : cs << 1;
		if((b & 0x40) != 0) cs ^= genpoly;
		cs = ((cs & 0x80000000) != 0) ? (cs << 1) ^ genpoly : cs << 1;
		if((b & 0x80) != 0) cs ^= genpoly;*/
		
		cs = ((cs & 0x80000000) != 0) ? (cs << 1) ^ genpoly : cs << 1;
		if((b & 0x80) != 0) cs ^= genpoly;
		cs = ((cs & 0x80000000) != 0) ? (cs << 1) ^ genpoly : cs << 1;
		if((b & 0x40) != 0) cs ^= genpoly;
		cs = ((cs & 0x80000000) != 0) ? (cs << 1) ^ genpoly : cs << 1;
		if((b & 0x20) != 0) cs ^= genpoly;
		cs = ((cs & 0x80000000) != 0) ? (cs << 1) ^ genpoly : cs << 1;
		if((b & 0x10) != 0) cs ^= genpoly;
		
		cs = ((cs & 0x80000000) != 0) ? (cs << 1) ^ genpoly : cs << 1;
		if((b & 0x08) != 0) cs ^= genpoly;
		cs = ((cs & 0x80000000) != 0) ? (cs << 1) ^ genpoly : cs << 1;
		if((b & 0x04) != 0) cs ^= genpoly;
		cs = ((cs & 0x80000000) != 0) ? (cs << 1) ^ genpoly : cs << 1;
		if((b & 0x02) != 0) cs ^= genpoly;
		cs = ((cs & 0x80000000) != 0) ? (cs << 1) ^ genpoly : cs << 1;
		if((b & 0x01) != 0) cs ^= genpoly;

		return cs;
	}
}
