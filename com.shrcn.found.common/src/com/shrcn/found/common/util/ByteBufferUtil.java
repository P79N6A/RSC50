/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED Log Analyzer.
 */
package com.shrcn.found.common.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * ByteBufferUtil.java
 * 
 * @author chenchun
 * @version 1.0, 2015-7-9
 */
public class ByteBufferUtil {

	/**
	 * 创建ByteBuffer
	 * @param len
	 * @return
	 */
	public static ByteBuffer getBuffer(int len) {
		return ByteBuffer.allocateDirect(len).order(ByteOrder.LITTLE_ENDIAN);
	}
	
	/**
	 * 获取ByteBuffer字节内容
	 * @param buff
	 * @return
	 */
	public static byte[] getData(ByteBuffer buff) {
		byte[] data = new byte[buff.position()];
		buff.flip();
		buff.get(data);
		return data;
	}
}
