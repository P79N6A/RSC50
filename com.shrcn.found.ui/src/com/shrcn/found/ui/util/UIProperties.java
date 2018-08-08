/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.ui.util;

import java.io.File;

import com.shrcn.found.common.Constants;
import com.shrcn.found.file.util.AProperties;
import com.shrcn.found.file.util.PropertyFileManager;

/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2014-5-28
 */
public class UIProperties extends AProperties {

	private String uiPath = "uiCfg.properties";
	private String path = "com/shrcn/found/ui/util/uiCfg.properties";

	private UIProperties() {
		properties = Constants.cfgDir + File.separator + uiPath;
		PropertyFileManager.initFile(data, properties, path, getClass());
	}

	private static volatile UIProperties instance = new UIProperties();

	/**
	 * 获取单例对象
	 */
	public static UIProperties getInstance() {
		if (null == instance) {
			synchronized (UIProperties.class) {
				if (null == instance) {
					instance = new UIProperties();
				}
			}
		}
		return instance;
	}
	
	public String getBuildID(){
		return getProperty("BUILD_ID");
	}
	
	public String getVersion() {
		String ver = getProperty("VERSION");
		if (ver==null){
			ver="";
		}
		return ver;
	}
}
