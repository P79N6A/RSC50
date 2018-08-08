/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.dbxapi.util;

import java.io.File;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.common.util.TimeCounter;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.xmldb.ProjectManager;
import com.shrcn.found.xmldb.XMLDBHelper;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-10-5
 */
public class TestUtil {
	
	public static void init(String scdfile, boolean reload) {
		String openFName = new File(scdfile).getName();
		String openedPrjName = openFName .substring(0, openFName.indexOf("."));
		String prjName = StringUtil.string2Unicode(openedPrjName);
		Constants.DEFAULT_PRJECT_NAME = prjName;
		
		FileManipulate.initDir(Constants.crc32Dir);
		FileManipulate.clearDirNow(Constants.crc32Dir);
		FileManipulate.initDir(".\\crc");
		FileManipulate.clearDirNow(".\\crc");
		
//		TimeCounter.begin();
		if (Constants.XQUERY) {
			if (reload)
				FileManipulate.deleteFiles(".\\data");
			if (!ProjectManager.existProject(prjName)) {
				ProjectManager.initPrjectDB(prjName);
				loadDoc(scdfile);
			}
		} else {
			loadDoc(scdfile);
		}
//		TimeCounter.end("加载" + scdfile);
	}
	
	public static void loadDoc(String scdfile) {
		try {
			XMLDBHelper.loadDocument(Constants.DEFAULT_SCD_DOC_NAME, scdfile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

