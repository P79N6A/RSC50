/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.mail.internet.MimeUtility;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-12-12
 */
/**
 * $Log: Base64.java,v $ Revision 1.2 2013/03/28 02:57:54 cchun Update:清理未使用引用
 * 
 * Revision 1.1 2012/12/13 01:18:03 cchun Update:添加配置文件加密处理
 * 
 */

public class Base64 {
	
	public static byte[] encode(byte[] b) throws Exception {
		ByteArrayOutputStream baos = null;
		OutputStream b64os = null;
		try {
			baos = new ByteArrayOutputStream();
			b64os = MimeUtility.encode(baos, "base64");
			b64os.write(b);
			b64os.close();
			return baos.toByteArray();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (baos != null) {
					baos.close();
					baos = null;
				}
			} catch (Exception e) {
			}
			try {
				if (b64os != null) {
					b64os.close();
					b64os = null;
				}
			} catch (Exception e) {
			}
		}
	}

	public static byte[] decode(byte[] b) throws Exception {
		ByteArrayInputStream bais = null;
		InputStream b64is = null;
		try {
			bais = new ByteArrayInputStream(b);
			b64is = MimeUtility.decode(bais, "base64");
			byte[] tmp = new byte[b.length];
			int n = b64is.read(tmp);
			byte[] res = new byte[n];
			System.arraycopy(tmp, 0, res, 0, n);
			return res;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (bais != null) {
					bais.close();
					bais = null;
				}
			} catch (Exception e) {
			}
			try {
				if (b64is != null) {
					b64is.close();
					b64is = null;
				}
			} catch (Exception e) {
			}
		}
	}
}
