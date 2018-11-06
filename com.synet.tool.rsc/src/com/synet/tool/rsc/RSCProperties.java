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
import com.synet.tool.rsc.jdbc.ConnParam;

public class RSCProperties extends AProperties {

	private String path = "com/synet/tool/rsc/rsc.properties";
	private static volatile RSCProperties instance = new RSCProperties();
	private Map<String, Integer> codeCache = new HashMap<>();
	private static final String ORA_IP = "ora.ip";
	private static final String ORA_PORT = "ora.port";
	private static final String ORA_DB = "ora.db";
	private static final String ORA_USR = "ora.usr";
	private static final String ORA_PWD = "ora.pwd";
	private static final String RULE = "RULE";

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

	public ConnParam getConnParam() {
		ConnParam cp = new ConnParam();
		cp.setIp(getProperty(ORA_IP));
		cp.setPort(getProperty(ORA_PORT));
		cp.setDbName(getProperty(ORA_DB));
		cp.setUser(getProperty(ORA_USR));
		cp.setPassword(getProperty(ORA_PWD));
		return cp;
	}
	
	public void saveConnParam(ConnParam cp) {
		setProperty(ORA_IP, cp.getIp(), false);
		setProperty(ORA_PORT, cp.getPort(), false);
		setProperty(ORA_DB, cp.getDbName(), false);
		setProperty(ORA_USR, cp.getUser(), false);
		setProperty(ORA_PWD, cp.getPassword(), false);
		saveData();
	}
	
	public String getCurrentRule() {
		return getProperty(RULE);
	}
	
	public void setCurrentRule(String fileName) {
		setProperty(RULE, fileName, true);
	}
}
