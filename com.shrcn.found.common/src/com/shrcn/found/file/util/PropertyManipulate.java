/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Visual Device Develop System.
 */

package com.shrcn.found.file.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * properties操作
 * 
 * @author 刘静
 * @version 1.0, 2009-06-03
 */
public class PropertyManipulate {

	/**
	 * 读取配置项
	 * 
	 * @param key-属性名称
	 * @return 属性值
	 * @throws IOException 
	 */
	public static String findProperty(String key, String filePath){
		File CfgFile = new File(filePath);
		FileInputStream in = null;
		try {
			if (!CfgFile.exists()) {
				CfgFile.createNewFile();// 没有则创建该文件,不过,该文件内容是空的
			}
			Properties prop = new Properties();
			in = new FileInputStream(CfgFile);
			prop.load(in);
			String value = ISO2GB(prop.getProperty(key));// 一般的配置文件是iso2的编码格式,将他转换为gb2312
			in.close();
			return value;
		} catch (IOException ex) {
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}

	public static Collection<Object> findValues(String filePath){
		File CfgFile = new File(filePath);
		FileInputStream in = null;
		try {
			if (!CfgFile.exists()) {
				CfgFile.createNewFile();// 没有则创建该文件,不过,该文件内容是空的
			}
			Properties prop = new Properties();
			in = new FileInputStream(CfgFile);
			prop.load(in);
			Collection<Object> values = prop.values();
			in.close();
			return values;
		} catch (IOException ex) {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}
	
	/**
	 * 将字符串中的反斜杠("\")替换成双反斜杠("\\")
	 * 
	 * @param pStr
	 *            需要替换的字符串
	 * @return 替换后的字符串
	 */
	public static String replaceBacklashToDouble(String pStr) {
		if (pStr == null)
			return ""; //$NON-NLS-1$
		if (pStr.length() < 1) {
			return ""; //$NON-NLS-1$
		}
		StringTokenizer stk = new StringTokenizer(pStr, "\\", true); //$NON-NLS-1$
		Vector<String> v = new Vector<String>();
		while (stk.hasMoreTokens()) {
			v.addElement(stk.nextToken());
		}

		String strResult = v.get(0).toString();
		for (int i = 1; i < v.size(); i++) {
			if (v.get(i).toString().trim().equals("\\")) { //$NON-NLS-1$
				strResult += "\\" + v.get(i).toString(); //$NON-NLS-1$
			} else {
				strResult += v.get(i).toString();
			}
		}
		return strResult;
	}

	/**
	 * 将ISO-8859-1字符编码转为GB2312字符编码
	 * 
	 * @param source
	 *            要进行转码的字符串
	 * @return 转码后的字符串
	 */
	private static String ISO2GB(String source) {
		if (source == null || source.length() == 0) {
			return ""; //$NON-NLS-1$
		}
		String target = source;
		try {
			target = new String(source.getBytes("8859_1"), "GB2312"); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(Messages.getString("PropertiesManipulate.turn_gb2312_failure")); //$NON-NLS-1$
		}
		return target;
	}

	public static void replaceProperty(String key, String value, String filePath) {
		File CfgFile = new File(filePath);
		FileOutputStream out = null;
		FileInputStream in = null;
		try {
			Properties prop = new Properties();
			// 文件不存在,则创建文件
			if (!CfgFile.exists()) {
				CfgFile.createNewFile();
			}
			in = new FileInputStream(CfgFile);
			prop.load(in);
			value = replaceBacklashToDouble(value);
			// 然后再改变属性值
			prop.setProperty(key, value);
			
			// 调用相关类,想文件中写入键值对
			out = new FileOutputStream(CfgFile);
			if(null != out) {
				prop.store(out, ""); //$NON-NLS-1$
				out.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(null != out)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
