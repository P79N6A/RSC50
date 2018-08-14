/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.XMLFileManager;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-11
 */
public class CfgCompareUtil {

	private static String compareIedType(String suffix, String local, String host) {
		String msg = "";
		TypeInfoHandler handler = new TypeInfoHandler();
		XMLFileManager.parseUTF8BySax(local, handler);
		String[] localInfos = handler.getInfos();
		handler = new TypeInfoHandler();
		XMLFileManager.parseUTF8BySax(host, handler);
		String[] hostInfos = handler.getInfos();
		String[] descs = new String[] {"厂商", "型号", "ICD配置版本"};
		
		String locVals = "";
		String hostVals = "";
		for (int i=0; i<3; i++) {
			String desc = descs[i];
			String locVal = localInfos[i];
			String hostVal = hostInfos[i];
			boolean match = true;
			if (locVal==null) {
				if (hostVal!=null)
					match = false;
			} else {
				if (!locVal.equals(hostVal))
					match = false;
			}
			if (!match) {
				if (!StringUtil.isEmpty(msg)) {
					msg += "、";
					locVals += "、";
					hostVals += "、";
				}
				msg += desc;
				locVals += StringUtil.getStrDesc(locVal);
				hostVals += StringUtil.getStrDesc(hostVal);
			}
		}
		if (!StringUtil.isEmpty(msg)) {
			return suffix + "文件的装置的" + msg + "不匹配。\n" +
					"本地：" + locVals + ";\n" +
					"装置：" + hostVals + "。\n";
		}
		return "";
	}

	public static String compareTypeInCID(String local, String host) {
		return compareIedType("CID", local, host);
	}
	
	public static String compareTypeInCCD(String local, String host) {
		return compareIedType("CCD", local, host);
	}
	
	static class TypeInfoHandler extends DefaultHandler {
		
		private String[] infos = new String[3];
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("IED".equals(qName)) {
				infos[0] = attributes.getValue(attributes.getIndex("manufacturer"));
				infos[1] = attributes.getValue(attributes.getIndex("type"));
				infos[2] = attributes.getValue(attributes.getIndex("configVersion"));
			}
		}
		
		public String[] getInfos() {
			return this.infos;
		}
	}
}

