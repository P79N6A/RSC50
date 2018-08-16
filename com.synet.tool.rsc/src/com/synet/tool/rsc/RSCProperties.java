/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.synet.tool.rsc;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.shrcn.found.file.util.AProperties;

public class RSCProperties extends AProperties {

	private String path = "com/synet/tool/rsc/rsc.properties";
	private static volatile RSCProperties instance = new RSCProperties();
	private Map<String, Integer> codeCache = new HashMap<>();

	private RSCProperties() {
		init(path);
		for (Object o : data.keySet()) {
			String key = (String) o;
			if (key.startsWith("code@")) {
				String value = data.getProperty(key);
				codeCache.put(key, Integer.parseInt(value));
			}
		}
	}

	/**
	 * 获取单例对象
	 */
	public static RSCProperties getInstance() {
		if (null == instance) {
			synchronized (RSCProperties.class) {
				if (null == instance) {
					instance = new RSCProperties();
				}
			}
		}
		return instance;
	}

	/**
	 * 得到下一个主键
	 * @return
	 */
	public String nextTbCode(String prefix) {
		String key = "code@" + prefix;
		Integer icode = codeCache.get(key);
		if (icode == null) {
			icode = 0;
		}
		String scode = data.getProperty(key);
		if (scode == null) {
			data.setProperty(key, DBConstants.CODE_STEP + "");
			saveData();
		} else {
			int imax = Integer.parseInt(scode);
			if (icode == imax) {
				imax += DBConstants.CODE_STEP;
				data.setProperty(key, imax + "");
				saveData();
			}
		}
		icode++;
		codeCache.put(key, icode);
		return prefix + toCode(icode);
	}
	
	private String toCode(int icode) {
		DecimalFormat df = new DecimalFormat("0000000000");
        return df.format(icode);
	}
	
}
