/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.dnd;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-4-7
 */
/**
 * $Log: JavaObjectTransfer.java,v $
 * Revision 1.1  2013/08/14 03:01:48  cchun
 * Add:添加
 *
 * Revision 1.3  2011/01/06 08:35:25  cchun
 * Update:清理注释
 *
 * Revision 1.2  2010/11/08 07:36:03  cchun
 * Update: 清除引用
 *
 * Revision 1.1  2010/04/08 08:26:59  cchun
 * Add:避免RTDBTable自己接受自己的数据
 *
 */
public class JavaObjectTransfer extends ByteArrayTransfer {
	private static JavaObjectTransfer instance = new JavaObjectTransfer();
	private static final String TYPE_NAME = "DCA-Points-transfer-format"; //$NON-NLS-1$
	private static final int TYPEID = registerType(TYPE_NAME);

	/**
	 * Returns the singleton inner-signal transfer instance.
	 */
	public static JavaObjectTransfer getInstance() {
		return instance;
	}

	/**
	 * Avoid explicit instantiation
	 */
	private JavaObjectTransfer() {
	}

	@Override
	protected int[] getTypeIds() {
		return new int[] { TYPEID };
	}

	@Override
	protected String[] getTypeNames() {
		return new String[] { TYPE_NAME };
	}
	
	/*
	 * Method declared on Transfer.
	 */
	public void javaToNative(Object object, TransferData transferData) {
		byte[] bytes = toByteArray(object);
		if (bytes != null)
			super.javaToNative(bytes, transferData);
	}
	
	/**
	 * 对象转为字节数组
	 * @param object
	 * @return
	 */
	public byte[] toByteArray(Object object) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(500);
		ObjectOutputStream os = null;
		byte[] bytes = null;
		try {
			os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
			os.writeObject(object);
			os.flush();
			bytes = byteStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != os)
					os.close();
				byteStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}

	/*
	 * Method declared on Transfer.
	 */
	public Object nativeToJava(TransferData transferData) {
		byte[] bytes = (byte[]) super.nativeToJava(transferData);
		return fromByteArray(bytes);
	}

	/**
	 * 从字节数组读取对象
	 * @param bytes
	 * @return
	 */
	public Object fromByteArray(byte[] bytes) {
		ByteArrayInputStream is = null;
		ObjectInputStream in = null;
		Object object = null;
		try {
			is = new ByteArrayInputStream(bytes);
			in = new ObjectInputStream(is);
			object = in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(null != in || null != is)
				try {
					is.close();
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return object;
	}
}
