/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.file.util;

import java.io.File;
import java.util.Properties;

import com.shrcn.found.common.Constants;

/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2014-5-28
 */
public abstract class AProperties {

	protected String properties = Constants.PROPERTIES_FILE;

	protected Properties data = new Properties();
	
	protected void init(String path) {
		String fileName = path.substring(path.lastIndexOf("/")+1);
		properties = com.shrcn.found.common.Constants.cfgDir + File.separator + fileName;
		PropertyFileManager.initFile(data, properties, path, getClass());
	}
	
	public String getProperty(String name) {
		return data.getProperty(name);
	}
	
	public void setProperty(String name, String value, boolean save) {
		data.setProperty(name, value);
		if (save)
			saveData();
	}
	
	public boolean isInProperty(String name, String value) {
		String property = getProperty(name);
		String[] strs = null;
		if (property != null) {
			strs = property.split(Constants.COMMA);
			for (String str : strs) {
				if (str.equals(value))
					return true;
			}
		}
		return false;
	}


	/**
	 * 保存系统配置
	 */
	public void saveData() {
		PropertyFileManager.saveFile(data, properties);
	}
	
	/**
	 * 获取字符串布尔值
	 * @param str
	 * @return
	 */
	public boolean getBoolValue(String str) {
		return Boolean.valueOf(data.getProperty(str));
	}
	
	public int getIntegerValue(String str) {
		return Integer.valueOf(data.getProperty(str));
	}
}
