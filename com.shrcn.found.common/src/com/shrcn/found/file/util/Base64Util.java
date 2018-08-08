/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-12-12
 */
/**
 * $Log: Base64Util.java,v $
 * Revision 1.2  2013/03/28 02:57:55  cchun
 * Update:清理未使用引用
 *
 * Revision 1.1  2012/12/13 01:18:03  cchun
 * Update:添加配置文件加密处理
 *
 */
public class Base64Util {

	public static void encryptFile(String path, String targetPath) {
		FileInputStream is = null;
		DataOutputStream os = null;
		try {
			is = new FileInputStream(path);
			byte[] data = new byte[is.available()];
			is.read(data);
			is.close();
			byte[] enData = Base64.encode(data);
			os = new DataOutputStream(new FileOutputStream(targetPath));
			os.write(enData);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (os != null)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void decryptFile(String path, String targetPath) {
		FileInputStream is = null;
		DataOutputStream os = null;
		try {
			is = new FileInputStream(path);
			byte[] data = new byte[is.available()];
			is.read(data);
			is.close();
			byte[] enData = Base64.decode(data);
			os = new DataOutputStream(new FileOutputStream(targetPath));
			os.write(enData);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (os != null)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
     * 加密文件夹。
     * @param dirpath
     * @param endfix
     */
    public static void encryptDir(String dirpath, String endfix) {
    	List<File> files = FileManipulate.findFiles(dirpath, endfix);
    	for (File file : files) {
    		String path = file.getPath();
    		encryptFile(path, path);
    	}
    }
	
	public static void main(String[] args) {
		encryptFile("C:/原始/Sub_hekou.rtu", "C:/原始/Sub_hekou1.rtu");
		decryptFile("C:/原始/Sub_hekou1.rtu", "C:/原始/Sub_hekou2.rtu");
	}
}
