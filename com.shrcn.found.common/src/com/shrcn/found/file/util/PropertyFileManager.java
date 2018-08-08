/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Visual Device Develop System.
 */
package com.shrcn.found.file.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;

/**
 * properties文件读写
 * 
 * @author 孙春颖
 * @version 1.0, 2014-05-28
 */
public class PropertyFileManager {

	/**
	 * 加载property文件
	 * 
	 * @param properties
	 * @param path
	 * @param clazz
	 */
	public static void loadRes(Properties properties, String path,
			Class<?> clazz) {
		InputStream is = null;
		try {
			ClassLoader loader = clazz.getClassLoader();
			is = loader.getResourceAsStream(path);
			properties.load(is);
		} catch (IOException e) {
			SCTLogger.error("加载properties:" + path + "失败！", e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				SCTLogger.error("关闭输入流错误。", e);
			}
		}
	}

	/**
	 * 加载property文件
	 * 
	 * @param properties
	 * @param file
	 */
	public static void loadFile(Properties properties, String file) {
		File f = new File(file);
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			properties.load(is);
		} catch (IOException e) {
			SCTLogger.error("加载properties:" + file + "失败！", e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				SCTLogger.error("关闭输入流错误。", e);
			}
		}
	}

	/**
	 * 初始化配置文件。
	 * 
	 * @param data
	 * @param file
	 * @param path
	 * @param contextClass
	 */
	public static void initFile(Properties data, String file, String path,
			Class<?> contextClass) {
		File f = new File(file);
		if (f.exists()) {
			loadFile(data, file);
		} else {
			loadRes(data, path, contextClass);
			saveFile(data, file);
		}
	}

	/**
	 * 保存系统配置
	 */
	public static void saveFile(Properties data, String file) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			if (null != out) {
				data.store(out, "Configuration File by SHR LTD."); //$NON-NLS-1$
				out.flush();
			}
		} catch (Exception e) {
			SCTLogger.error(e.getMessage());
		} finally {
			try {
				if (null != out)
					out.close();
			} catch (IOException e) {
				SCTLogger.error(e.getMessage());
			}
		}
	}
}
