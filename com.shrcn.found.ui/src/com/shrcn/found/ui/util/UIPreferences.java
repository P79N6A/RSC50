/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
/**
 * 
 */
package com.shrcn.found.ui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.StringUtil;

/**
 * 
 * @author zhouhuiming(mailto:zhm.3119@shrcn.com)
 * @version 1.0, 2010-8-18
 */
/**
 * $Log: UIPreferences.java,v $
 * Revision 1.5  2013/11/05 00:59:58  cxc
 * fix bug：修复最后一台装置删除不掉的问题
 *
 * Revision 1.4  2013/10/29 01:23:06  cxc
 * update:添加删除的方法
 *
 * Revision 1.3  2013/07/03 11:58:19  cchun
 * Update:改进addInfo()，避免重复添加
 *
 * Revision 1.2  2013/07/02 06:40:08  cchun
 * Update:添加addInfo()和getInfos()
 *
 * Revision 1.1  2013/03/29 09:36:27  cchun
 * Add:创建
 *
 * Revision 1.1  2010/09/03 02:45:52  cchun
 * Refactor:重构类名
 *
 * Revision 1.1  2010/08/24 01:27:21  cchun
 * Add:界面操作习惯管理工具
 *
 */
public class UIPreferences {
	private static final String cfg = Constants.cfgDir + File.separator
			+ "ui.properties";
	private static final Properties prop = new Properties();
	private static final String cfgPath = "com/shrcn/found/ui/util/ui.properties";
	private static UIPreferences sctPref = new UIPreferences();

	private UIPreferences() {
	}

	public static UIPreferences newInstance() {
		if (sctPref == null) {
			synchronized (UIPreferences.class) {
				if (sctPref == null)
					sctPref = new UIPreferences();
			}
		}

		return sctPref;
	}

	/* 从property中读取列配置文件 */
	static {
		InputStream is = null;
		try {
			File cfgFile = new File(cfg);
			if (!cfgFile.exists()) {
				is = UIPreferences.class.getClassLoader().getResourceAsStream(
						cfgPath);
				if (is != null) {
					prop.load(is);
				}
				saveCfg();
			} else {
				prop.load(new FileInputStream(cfg));
			}
		} catch (FileNotFoundException e) {
			SCTLogger.error("文件未找到异常：" + e.getMessage());
		} catch (IOException e) {
			SCTLogger.error("IO操作异常：" + e.getMessage());
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				SCTLogger.error("IO操作异常：" + e.getMessage());
			}
		}
	}

	public String getInfo(String key) {
		String value = prop.getProperty(key);
		return (value==null) ? "" : value;
	}
	
	public String[] getInfos(String key) {
		String info = getInfo(key);
		if (StringUtil.isEmpty(info))
			return new String[0];
		return info.split(",");
	}
	
	public void addInfo(String key, String value) {
		String info = getInfo(key);
		if (!StringUtil.isEmpty(info)) {
			if (info.indexOf(value) > -1)
				return;
			value = info + "," + value;
		}
		setInfo(key, value);
	}

	public void setInfo(String key, String value) {
		prop.put(key, value);
		saveCfg();
	}
	
	public void removeInfo(String key, String value){
		String[] infos = getInfos(key);
		String info = "";
		prop.remove(key);
		for (String obj : infos) {
			if (!value.equals(obj)) {
				if (!StringUtil.isEmpty(info)) {
					info = info + "," + obj;
				}else {
					info = obj;
				}
			}
		}
		setInfo(key, info);
	}

	/**
	 * 把改变的列配置保存到properties属性文件(注:xml文件不变)
	 */
	private static void saveCfg() {
		FileOutputStream fops = null;
		try {
			fops = new FileOutputStream(cfg);
			prop.store(fops, "");
		} catch (FileNotFoundException e) {
			SCTLogger.error(" 文件读取异常: " + e.getMessage());
		} catch (IOException e) {
			SCTLogger.error(" IO操作异常:  " + e.getMessage());
		} finally {
			try {
				if (fops != null) {
					fops.flush();
					fops.close();
				}
			} catch (IOException e) {
				SCTLogger.error(" IO操作异常:  " + e.getMessage());
			}
		}
	}

}
